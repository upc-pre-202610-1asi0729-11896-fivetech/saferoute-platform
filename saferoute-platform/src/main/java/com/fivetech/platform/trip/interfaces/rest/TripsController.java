package com.acme.saferoute.platform.trip.interfaces.rest;

import com.acme.saferoute.platform.shared.application.result.ApplicationError;
import com.acme.saferoute.platform.shared.application.result.Result;
import com.acme.saferoute.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
import com.acme.saferoute.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import com.acme.saferoute.platform.trip.application.commandservices.TripCommandService;
import com.acme.saferoute.platform.trip.application.queryservices.TripQueryService;
import com.acme.saferoute.platform.trip.domain.model.commands.CompleteTripCommand;
import com.acme.saferoute.platform.trip.domain.model.commands.DeleteTripCommand;
import com.acme.saferoute.platform.trip.domain.model.queries.GetAllTripsByOrganizationIdQuery;
import com.acme.saferoute.platform.trip.domain.model.queries.GetAttendancesByTripIdQuery;
import com.acme.saferoute.platform.trip.domain.model.queries.GetIncidentsByTripIdQuery;
import com.acme.saferoute.platform.trip.domain.model.queries.GetTripByIdQuery;
import com.acme.saferoute.platform.trip.interfaces.rest.resources.AttendanceResource;
import com.acme.saferoute.platform.trip.interfaces.rest.resources.IncidentResource;
import com.acme.saferoute.platform.trip.interfaces.rest.resources.ReportIncidentResource;
import com.acme.saferoute.platform.trip.interfaces.rest.resources.StartTripResource;
import com.acme.saferoute.platform.trip.interfaces.rest.resources.TripResource;
import com.acme.saferoute.platform.trip.interfaces.rest.resources.UpdateBoardingResource;
import com.acme.saferoute.platform.trip.interfaces.rest.transform.AttendanceResourceFromEntityAssembler;
import com.acme.saferoute.platform.trip.interfaces.rest.transform.IncidentResourceFromEntityAssembler;
import com.acme.saferoute.platform.trip.interfaces.rest.transform.ReportIncidentCommandFromResourceAssembler;
import com.acme.saferoute.platform.trip.interfaces.rest.transform.StartTripCommandFromResourceAssembler;
import com.acme.saferoute.platform.trip.interfaces.rest.transform.TripResourceFromEntityAssembler;
import com.acme.saferoute.platform.trip.interfaces.rest.transform.UpdateBoardingStatusCommandFromResourceAssembler;
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

/**
 * REST controller exposing trip execution and monitoring endpoints for the Trip bounded context
 * (US-10, US-11, US-14, US-20).
 *
 * <p>Controllers are intentionally thin: they translate resources to commands/queries, delegate to
 * the application services, and map results to HTTP responses via the shared assemblers.</p>
 */
@RestController
@RequestMapping(value = "/api/v1/trips", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Trips", description = "Trip execution and monitoring endpoints")
public class TripsController {

    private final TripCommandService tripCommandService;
    private final TripQueryService tripQueryService;

    /**
     * Creates the controller with the trip application services.
     *
     * @param tripCommandService the trip command service
     * @param tripQueryService   the trip query service
     */
    public TripsController(TripCommandService tripCommandService, TripQueryService tripQueryService) {
        this.tripCommandService = tripCommandService;
        this.tripQueryService = tripQueryService;
    }

    /**
     * Starts a new trip (US-10).
     *
     * @param resource the start-trip payload
     * @return the started trip resource, or an error response
     */
    @PostMapping
    @Operation(summary = "Start a trip", description = "Starts a trip in IN_PROGRESS state for a route and driver.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trip started",
                    content = @Content(schema = @Schema(implementation = TripResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<?> startTrip(@Valid @RequestBody StartTripResource resource) {
        var command = StartTripCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = tripCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                TripResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.CREATED);
    }

    /**
     * Lists trips, optionally filtered by organization.
     *
     * @param organizationId the owning organization identifier (optional)
     * @return the list of trip resources
     */
    @GetMapping
    @Operation(summary = "Get trips", description = "Retrieves trips, optionally filtered by organization.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trips found",
                    content = @Content(schema = @Schema(implementation = TripResource.class)))
    })
    public ResponseEntity<List<TripResource>> getTrips(
            @Parameter(description = "Owning organization identifier", example = "1")
            @RequestParam(required = false) Long organizationId) {
        var trips = organizationId == null
                ? java.util.List.<com.acme.saferoute.platform.trip.domain.model.aggregates.Trip>of()
                : tripQueryService.handle(new GetAllTripsByOrganizationIdQuery(organizationId));
        var resources = trips.stream().map(TripResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    /**
     * Retrieves a single trip by its identifier.
     *
     * @param tripId the trip identifier
     * @return the trip resource, or a 404 error response
     */
    @GetMapping("/{tripId}")
    @Operation(summary = "Get trip by id", description = "Retrieves a trip by its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trip found",
                    content = @Content(schema = @Schema(implementation = TripResource.class))),
            @ApiResponse(responseCode = "404", description = "Trip not found")
    })
    public ResponseEntity<?> getTripById(
            @Parameter(description = "Trip identifier", example = "1", required = true)
            @PathVariable Long tripId) {
        var trip = tripQueryService.handle(new GetTripByIdQuery(tripId));
        if (trip.isEmpty()) {
            return ErrorResponseAssembler.toErrorResponseFromApplicationError(
                    ApplicationError.notFound("Trip", tripId.toString()));
        }
        return ResponseEntity.ok(TripResourceFromEntityAssembler.toResourceFromEntity(trip.get()));
    }

    /**
     * Deletes a trip by its identifier.
     *
     * @param tripId the trip identifier
     * @return 204 on success, or a 404 error response
     */
    @DeleteMapping("/{tripId}")
    @Operation(summary = "Delete a trip", description = "Deletes a trip and its attendance/incident children.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Trip deleted"),
            @ApiResponse(responseCode = "404", description = "Trip not found")
    })
    public ResponseEntity<?> deleteTrip(
            @Parameter(description = "Trip identifier", example = "1", required = true)
            @PathVariable Long tripId) {
        var result = tripCommandService.handle(new DeleteTripCommand(tripId));
        if (result.isFailure()) {
            return ErrorResponseAssembler.toErrorResponseFromApplicationError(
                    ((Result.Failure<Long, ApplicationError>) result).error());
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * Completes a trip (US-14).
     *
     * @param tripId the trip identifier
     * @return the completed trip resource, or an error response
     */
    @PostMapping("/{tripId}/completion")
    @Operation(summary = "Complete a trip",
            description = "Finishes a trip. Fails if children are still on board (US-14 S2).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trip completed",
                    content = @Content(schema = @Schema(implementation = TripResource.class))),
            @ApiResponse(responseCode = "404", description = "Trip not found"),
            @ApiResponse(responseCode = "422", description = "Trip cannot be completed (e.g. children on board)")
    })
    public ResponseEntity<?> completeTrip(
            @Parameter(description = "Trip identifier", example = "1", required = true)
            @PathVariable Long tripId) {
        var result = tripCommandService.handle(new CompleteTripCommand(tripId));
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                TripResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.OK);
    }

    /**
     * Updates a child's boarding status during a trip (US-11).
     *
     * @param tripId   the trip identifier
     * @param resource the update-boarding payload
     * @return the updated trip resource, or an error response
     */
    @PatchMapping("/{tripId}/attendances")
    @Operation(summary = "Update boarding status",
            description = "Marks a child as boarded, absent or dropped off during a trip.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Boarding status updated",
                    content = @Content(schema = @Schema(implementation = TripResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Trip not found"),
            @ApiResponse(responseCode = "422", description = "Boarding cannot be updated (trip not in progress)")
    })
    public ResponseEntity<?> updateBoardingStatus(
            @Parameter(description = "Trip identifier", example = "1", required = true)
            @PathVariable Long tripId,
            @Valid @RequestBody UpdateBoardingResource resource) {
        var command = UpdateBoardingStatusCommandFromResourceAssembler.toCommandFromResource(tripId, resource);
        var result = tripCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                TripResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.OK);
    }

    /**
     * Lists the attendance records of a trip.
     *
     * @param tripId the trip identifier
     * @return the list of attendance resources
     */
    @GetMapping("/{tripId}/attendances")
    @Operation(summary = "Get attendances by trip", description = "Retrieves the boarding records of a trip.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attendances found",
                    content = @Content(schema = @Schema(implementation = AttendanceResource.class)))
    })
    public ResponseEntity<List<AttendanceResource>> getAttendancesByTrip(
            @Parameter(description = "Trip identifier", example = "1", required = true)
            @PathVariable Long tripId) {
        var attendances = tripQueryService.handle(new GetAttendancesByTripIdQuery(tripId));
        var resources = attendances.stream()
                .map(attendance -> AttendanceResourceFromEntityAssembler.toResourceFromEntity(tripId, attendance))
                .toList();
        return ResponseEntity.ok(resources);
    }

    /**
     * Reports an incident on a trip.
     *
     * @param tripId   the trip identifier
     * @param resource the report-incident payload
     * @return the updated trip resource, or an error response
     */
    @PostMapping("/{tripId}/incidents")
    @Operation(summary = "Report an incident", description = "Reports an incident that occurred during a trip.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Incident reported",
                    content = @Content(schema = @Schema(implementation = TripResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Trip not found"),
            @ApiResponse(responseCode = "422", description = "Incident cannot be reported on a completed trip")
    })
    public ResponseEntity<?> reportIncident(
            @Parameter(description = "Trip identifier", example = "1", required = true)
            @PathVariable Long tripId,
            @Valid @RequestBody ReportIncidentResource resource) {
        var command = ReportIncidentCommandFromResourceAssembler.toCommandFromResource(tripId, resource);
        var result = tripCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                TripResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.CREATED);
    }

    /**
     * Lists the incidents reported on a trip.
     *
     * @param tripId the trip identifier
     * @return the list of incident resources
     */
    @GetMapping("/{tripId}/incidents")
    @Operation(summary = "Get incidents by trip", description = "Retrieves the incidents reported on a trip.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Incidents found",
                    content = @Content(schema = @Schema(implementation = IncidentResource.class)))
    })
    public ResponseEntity<List<IncidentResource>> getIncidentsByTrip(
            @Parameter(description = "Trip identifier", example = "1", required = true)
            @PathVariable Long tripId) {
        var incidents = tripQueryService.handle(new GetIncidentsByTripIdQuery(tripId));
        var resources = incidents.stream()
                .map(incident -> IncidentResourceFromEntityAssembler.toResourceFromEntity(tripId, incident))
                .toList();
        return ResponseEntity.ok(resources);
    }
}
