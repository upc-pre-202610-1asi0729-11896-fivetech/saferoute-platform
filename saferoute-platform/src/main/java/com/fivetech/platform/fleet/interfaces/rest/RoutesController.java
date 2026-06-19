package com.fivetech.platform.fleet.interfaces.rest;

import com.fivetech.platform.fleet.application.commandservices.AssignmentCommandService;
import com.fivetech.platform.fleet.application.commandservices.RouteCommandService;
import com.fivetech.platform.fleet.application.queryservices.AssignmentQueryService;
import com.fivetech.platform.fleet.application.queryservices.RouteQueryService;
import com.fivetech.platform.fleet.application.queryservices.VehicleQueryService;
import com.fivetech.platform.fleet.domain.model.aggregates.Assignment;
import com.fivetech.platform.fleet.domain.model.aggregates.Route;
import com.fivetech.platform.fleet.domain.model.aggregates.Vehicle;
import com.fivetech.platform.fleet.domain.model.entities.Stop;
import com.fivetech.platform.fleet.domain.model.commands.ActivateRouteCommand;
import com.fivetech.platform.fleet.domain.model.commands.CreateAssignmentCommand;
import com.fivetech.platform.fleet.domain.model.commands.DeleteRouteCommand;
import com.fivetech.platform.fleet.domain.model.commands.ReplaceRouteStopsCommand;
import com.fivetech.platform.fleet.domain.model.commands.RouteStopData;
import com.fivetech.platform.fleet.domain.model.commands.UpdateAssignmentCommand;
import com.fivetech.platform.fleet.domain.model.queries.GetAllRoutesByOrganizationIdQuery;
import com.fivetech.platform.fleet.domain.model.queries.GetAssignmentByRouteIdQuery;
import com.fivetech.platform.fleet.domain.model.queries.GetRouteByIdQuery;
import com.fivetech.platform.fleet.domain.model.queries.GetStopsByRouteIdQuery;
import com.fivetech.platform.fleet.domain.model.queries.GetVehicleByIdQuery;
import com.fivetech.platform.fleet.domain.model.valueobjects.ChildId;
import com.fivetech.platform.fleet.interfaces.rest.resources.AddStopResource;
import com.fivetech.platform.fleet.interfaces.rest.resources.RouteResource;
import com.fivetech.platform.fleet.interfaces.rest.resources.StopResource;
import com.fivetech.platform.fleet.interfaces.rest.transform.AddStopToRouteCommandFromResourceAssembler;
import com.fivetech.platform.fleet.interfaces.rest.transform.CreateRouteCommandFromResourceAssembler;
import com.fivetech.platform.fleet.interfaces.rest.transform.RouteResourceFromEntityAssembler;
import com.fivetech.platform.fleet.interfaces.rest.transform.StopResourceFromEntityAssembler;
import com.fivetech.platform.fleet.interfaces.rest.transform.UpdateRouteCommandFromResourceAssembler;
import com.fivetech.platform.shared.application.result.ApplicationError;
import com.fivetech.platform.shared.application.result.Result;
import com.fivetech.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller exposing route management endpoints for the Fleet bounded context.
 *
 * <p>This controller speaks the flat {@link RouteResource} contract used by the SafeRoute clients.
 * The decomposition between the flat resource and the decomposed domain (Route + Stop + Vehicle +
 * Assignment) is handled by the transform assemblers and small lookups here, keeping the domain
 * untouched:</p>
 * <ul>
 *   <li>the route's {@code Stop} children are exposed via the dedicated {@code /routes/{id}/stops}
 *       endpoints (not nested in the route resource);</li>
 *   <li>{@code driverId}/{@code studentIds} round-trip to the route's {@code Assignment};</li>
 *   <li>{@code vehiclePlate} is resolved from the {@code Vehicle} aggregate (read-only);</li>
 *   <li>{@code driverName} belongs to the Stakeholder context (not implemented) and returns empty.</li>
 * </ul>
 */
@RestController
@RequestMapping(value = "/api/v1/routes", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Routes", description = "Route management endpoints")
public class RoutesController {

    private static final String STATUS_ACTIVE = "ACTIVE";

    private final RouteCommandService routeCommandService;
    private final RouteQueryService routeQueryService;
    private final VehicleQueryService vehicleQueryService;
    private final AssignmentCommandService assignmentCommandService;
    private final AssignmentQueryService assignmentQueryService;

    /**
     * Creates the controller with the Fleet application services it composes.
     *
     * @param routeCommandService      the route command service
     * @param routeQueryService        the route query service
     * @param vehicleQueryService      the vehicle query service (resolves the assigned plate)
     * @param assignmentCommandService the assignment command service (staffing round-trip)
     * @param assignmentQueryService   the assignment query service (staffing round-trip)
     */
    public RoutesController(RouteCommandService routeCommandService,
                            RouteQueryService routeQueryService,
                            VehicleQueryService vehicleQueryService,
                            AssignmentCommandService assignmentCommandService,
                            AssignmentQueryService assignmentQueryService) {
        this.routeCommandService = routeCommandService;
        this.routeQueryService = routeQueryService;
        this.vehicleQueryService = vehicleQueryService;
        this.assignmentCommandService = assignmentCommandService;
        this.assignmentQueryService = assignmentQueryService;
    }

    /**
     * Lists routes, optionally filtered by organization.
     *
     * @param organizationId the owning organization identifier (optional)
     * @return the list of flat route resources
     */
    @GetMapping
    @Operation(summary = "Get routes", description = "Retrieves routes, optionally filtered by organization.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Routes found",
                    content = @Content(schema = @Schema(implementation = RouteResource.class)))
    })
    public ResponseEntity<List<RouteResource>> getRoutes(
            @Parameter(description = "Owning organization identifier", example = "1")
            @RequestParam(required = false) Long organizationId) {
        List<Route> routes = organizationId == null
                ? List.of()
                : routeQueryService.handle(new GetAllRoutesByOrganizationIdQuery(organizationId));
        var resources = routes.stream().map(this::toResource).toList();
        return ResponseEntity.ok(resources);
    }

    /**
     * Retrieves a single route by its identifier.
     *
     * @param id the route identifier
     * @return the flat route resource, or a 404 error response
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get route by id", description = "Retrieves a route by its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Route found",
                    content = @Content(schema = @Schema(implementation = RouteResource.class))),
            @ApiResponse(responseCode = "404", description = "Route not found")
    })
    public ResponseEntity<?> getRouteById(
            @Parameter(description = "Route identifier", example = "1", required = true)
            @PathVariable Long id) {
        var route = routeQueryService.handle(new GetRouteByIdQuery(id));
        if (route.isEmpty()) {
            return ErrorResponseAssembler.toErrorResponseFromApplicationError(
                    ApplicationError.notFound("Route", id.toString()));
        }
        return ResponseEntity.ok(toResource(route.get()));
    }

    /**
     * Creates a new route (US-5) from the flat resource and, when driver data is present, its
     * staffing assignment (US-6). When the requested status is ACTIVE, the route is activated right
     * after creation. Stops are added separately through the {@code /routes/{id}/stops} endpoints.
     *
     * @param resource the inbound flat route resource
     * @return the created flat route resource, or an error response
     */
    @PostMapping
    @Operation(summary = "Create a route",
            description = "Creates a route and optional driver/children assignment (stops via /routes/{id}/stops).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Route created",
                    content = @Content(schema = @Schema(implementation = RouteResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<?> createRoute(@Valid @RequestBody RouteResource resource) {
        var command = CreateRouteCommandFromResourceAssembler.toCommandFromResource(resource);
        var createResult = routeCommandService.handle(command);
        if (createResult.isFailure()) {
            return ErrorResponseAssembler.toErrorResponseFromApplicationError(
                    ((Result.Failure<Route, ApplicationError>) createResult).error());
        }

        var route = ((Result.Success<Route, ApplicationError>) createResult).value();

        // Honor the requested ACTIVE status by activating the freshly created (DRAFT) route.
        if (STATUS_ACTIVE.equalsIgnoreCase(resource.status())) {
            var activateResult = routeCommandService.handle(new ActivateRouteCommand(route.getId()));
            if (activateResult.isSuccess()) {
                route = ((Result.Success<Route, ApplicationError>) activateResult).value();
            }
        }

        syncStaffing(route.getId(), resource);
        return new ResponseEntity<>(toResource(route), HttpStatus.CREATED);
    }

    /**
     * Updates a route's information and staffing from the flat resource. Stops are managed
     * separately through the {@code /routes/{id}/stops} endpoints.
     *
     * @param id       the route identifier from the path
     * @param resource the inbound flat route resource
     * @return the updated flat route resource, or an error response
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a route",
            description = "Updates the route's information and driver/children assignment.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Route updated",
                    content = @Content(schema = @Schema(implementation = RouteResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Route not found")
    })
    public ResponseEntity<?> updateRoute(
            @Parameter(description = "Route identifier", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody RouteResource resource) {
        var command = UpdateRouteCommandFromResourceAssembler.toCommandFromResource(id, resource);
        var updateResult = routeCommandService.handle(command);
        if (updateResult.isFailure()) {
            return ErrorResponseAssembler.toErrorResponseFromApplicationError(
                    ((Result.Failure<Route, ApplicationError>) updateResult).error());
        }

        var route = ((Result.Success<Route, ApplicationError>) updateResult).value();
        syncStaffing(route.getId(), resource);
        return ResponseEntity.ok(toResource(route));
    }

    /**
     * Deletes a route by its identifier (its assignment is removed as well).
     *
     * @param id the route identifier
     * @return 204 on success, or a 404 error response
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a route", description = "Deletes a route, its stops and its assignment.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Route deleted"),
            @ApiResponse(responseCode = "404", description = "Route not found")
    })
    public ResponseEntity<?> deleteRoute(
            @Parameter(description = "Route identifier", example = "1", required = true)
            @PathVariable Long id) {
        var result = routeCommandService.handle(new DeleteRouteCommand(id));
        if (result.isFailure()) {
            return ErrorResponseAssembler.toErrorResponseFromApplicationError(
                    ((Result.Failure<Long, ApplicationError>) result).error());
        }
        return ResponseEntity.noContent().build();
    }

    // ─────────────────────────── Stops (PUML: addStop / getStopsByRoute) ───────────────────────────

    /**
     * Lists the ordered stops of a route (PUML: {@code getStopsByRoute}).
     *
     * @param routeId the route identifier
     * @return the route's stops as resources
     */
    @GetMapping("/{routeId}/stops")
    @Operation(summary = "Get stops by route", description = "Retrieves the ordered stops of a route.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stops found",
                    content = @Content(schema = @Schema(implementation = StopResource.class)))
    })
    public ResponseEntity<List<StopResource>> getStopsByRoute(
            @Parameter(description = "Route identifier", example = "1", required = true)
            @PathVariable Long routeId) {
        var stops = routeQueryService.handle(new GetStopsByRouteIdQuery(routeId));
        var resources = stops.stream()
                .map(stop -> StopResourceFromEntityAssembler.toResourceFromEntity(routeId, stop))
                .toList();
        return ResponseEntity.ok(resources);
    }

    /**
     * Adds a single stop to a route (PUML: {@code addStop}).
     *
     * @param routeId  the route identifier
     * @param resource the stop payload
     * @return the route's stops after adding, or an error response
     */
    @PostMapping("/{routeId}/stops")
    @Operation(summary = "Add a stop to a route", description = "Adds one stop to the route's sequence.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Stop added",
                    content = @Content(schema = @Schema(implementation = StopResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Route not found")
    })
    public ResponseEntity<?> addStop(
            @Parameter(description = "Route identifier", example = "1", required = true)
            @PathVariable Long routeId,
            @Valid @RequestBody AddStopResource resource) {
        var command = AddStopToRouteCommandFromResourceAssembler.toCommandFromResource(routeId, resource);
        var result = routeCommandService.handle(command);
        if (result.isFailure()) {
            return ErrorResponseAssembler.toErrorResponseFromApplicationError(
                    ((Result.Failure<Route, ApplicationError>) result).error());
        }
        var route = ((Result.Success<Route, ApplicationError>) result).value();
        return new ResponseEntity<>(stopsOf(route), HttpStatus.CREATED);
    }

    /**
     * Replaces the route's entire stop sequence (bulk set, used by the edit flow).
     *
     * @param routeId   the route identifier
     * @param resources the full desired stop list
     * @return the route's stops after replacement, or an error response
     */
    @PutMapping("/{routeId}/stops")
    @Operation(summary = "Replace route stops", description = "Replaces the route's whole stop sequence.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stops replaced",
                    content = @Content(schema = @Schema(implementation = StopResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Route not found")
    })
    public ResponseEntity<?> replaceStops(
            @Parameter(description = "Route identifier", example = "1", required = true)
            @PathVariable Long routeId,
            @RequestBody List<AddStopResource> resources) {
        List<RouteStopData> stops = (resources == null ? List.<AddStopResource>of() : resources).stream()
                .map(r -> new RouteStopData(r.name(), r.latitude(), r.longitude(), r.stopOrder()))
                .toList();
        var result = routeCommandService.handle(new ReplaceRouteStopsCommand(routeId, stops));
        if (result.isFailure()) {
            return ErrorResponseAssembler.toErrorResponseFromApplicationError(
                    ((Result.Failure<Route, ApplicationError>) result).error());
        }
        var route = ((Result.Success<Route, ApplicationError>) result).value();
        return ResponseEntity.ok(stopsOf(route));
    }

    /**
     * Maps a route's stops (ordered) to their output resources.
     *
     * @param route the route aggregate
     * @return the ordered stop resources
     */
    private List<StopResource> stopsOf(Route route) {
        return route.getStops().stream()
                .sorted(java.util.Comparator.comparingInt(Stop::getStopOrder))
                .map(stop -> StopResourceFromEntityAssembler.toResourceFromEntity(route.getId(), stop))
                .toList();
    }

    /**
     * Upserts the route's staffing (Assignment aggregate) from the flat resource: creates the
     * assignment when the resource carries a driver and none exists yet, or replaces the staffing
     * when one already exists. A resource without driver leaves the staffing untouched.
     *
     * @param routeId  the staffed route identifier
     * @param resource the inbound flat route resource carrying driverId/studentIds
     */
    private void syncStaffing(Long routeId, RouteResource resource) {
        if (resource.driverId() == null) {
            return;
        }
        List<Long> studentIds = resource.studentIds() == null ? List.of() : resource.studentIds();
        boolean exists = assignmentQueryService.handle(new GetAssignmentByRouteIdQuery(routeId)).isPresent();
        if (exists) {
            assignmentCommandService.handle(new UpdateAssignmentCommand(routeId, resource.driverId(), studentIds));
        } else {
            assignmentCommandService.handle(new CreateAssignmentCommand(routeId, resource.driverId(), studentIds));
        }
    }

    /**
     * Builds the flat resource for a route, resolving the assigned vehicle's plate and the staffing
     * data (driver/children) from their owning aggregates.
     *
     * @param route the route aggregate
     * @return the flat route resource
     */
    private RouteResource toResource(Route route) {
        String plate = "";
        if (route.getVehicleId() != null) {
            plate = vehicleQueryService.handle(new GetVehicleByIdQuery(route.getVehicleId().vehicleId()))
                    .map(Vehicle::getPlate)
                    .orElse("");
        }

        Optional<Assignment> assignment = assignmentQueryService.handle(new GetAssignmentByRouteIdQuery(route.getId()));
        Long driverId = assignment.map(a -> a.getDriverId().driverId()).orElse(null);
        List<Long> studentIds = assignment
                .map(a -> a.getChildIds().stream().map(ChildId::childId).toList())
                .orElse(List.of());

        return RouteResourceFromEntityAssembler.toResourceFromEntity(route, plate, driverId, studentIds);
    }
}
