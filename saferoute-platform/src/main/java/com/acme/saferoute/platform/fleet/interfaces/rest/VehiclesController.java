package com.acme.saferoute.platform.fleet.interfaces.rest;

import com.acme.saferoute.platform.fleet.application.commandservices.VehicleCommandService;
import com.acme.saferoute.platform.fleet.application.queryservices.VehicleQueryService;
import com.acme.saferoute.platform.fleet.domain.model.commands.DeleteVehicleCommand;
import com.acme.saferoute.platform.fleet.domain.model.queries.GetAllVehiclesByOrganizationIdQuery;
import com.acme.saferoute.platform.fleet.domain.model.queries.GetVehicleByIdQuery;
import com.acme.saferoute.platform.fleet.interfaces.rest.resources.VehicleResource;
import com.acme.saferoute.platform.fleet.interfaces.rest.transform.CreateVehicleCommandFromResourceAssembler;
import com.acme.saferoute.platform.fleet.interfaces.rest.transform.UpdateVehicleCommandFromResourceAssembler;
import com.acme.saferoute.platform.fleet.interfaces.rest.transform.VehicleResourceFromEntityAssembler;
import com.acme.saferoute.platform.shared.application.result.ApplicationError;
import com.acme.saferoute.platform.shared.application.result.Result;
import com.acme.saferoute.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
import com.acme.saferoute.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
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
 * REST controller exposing vehicle management endpoints for the Fleet bounded context.
 *
 * <p>Speaks the flat {@link VehicleResource} contract used by the SafeRoute clients (used for both
 * request and response bodies).</p>
 */
@RestController
@RequestMapping(value = "/api/v1/vehicles", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Vehicles", description = "Vehicle management endpoints")
public class VehiclesController {

    private final VehicleCommandService vehicleCommandService;
    private final VehicleQueryService vehicleQueryService;

    /**
     * Creates the controller with the vehicle application services.
     *
     * @param vehicleCommandService the vehicle command service
     * @param vehicleQueryService   the vehicle query service
     */
    public VehiclesController(VehicleCommandService vehicleCommandService, VehicleQueryService vehicleQueryService) {
        this.vehicleCommandService = vehicleCommandService;
        this.vehicleQueryService = vehicleQueryService;
    }

    /**
     * Registers a new vehicle.
     *
     * @param resource the vehicle payload
     * @return the created vehicle resource, or an error response
     */
    @PostMapping
    @Operation(summary = "Create a new vehicle", description = "Registers a vehicle in an organization's fleet.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vehicle created",
                    content = @Content(schema = @Schema(implementation = VehicleResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Conflict - vehicle plate already exists")
    })
    public ResponseEntity<?> createVehicle(@Valid @RequestBody VehicleResource resource) {
        var command = CreateVehicleCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = vehicleCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                VehicleResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.CREATED);
    }

    /**
     * Updates an existing vehicle.
     *
     * @param vehicleId the vehicle identifier from the path
     * @param resource  the vehicle payload
     * @return the updated vehicle resource, or an error response
     */
    @PutMapping("/{vehicleId}")
    @Operation(summary = "Update a vehicle", description = "Updates a vehicle's information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle updated",
                    content = @Content(schema = @Schema(implementation = VehicleResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    public ResponseEntity<?> updateVehicle(
            @Parameter(description = "Vehicle identifier", example = "1", required = true)
            @PathVariable Long vehicleId,
            @Valid @RequestBody VehicleResource resource) {
        var command = UpdateVehicleCommandFromResourceAssembler.toCommandFromResource(vehicleId, resource);
        var result = vehicleCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                VehicleResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.OK);
    }

    /**
     * Lists vehicles, optionally filtered by organization.
     *
     * @param organizationId the owning organization identifier (optional)
     * @return the list of vehicle resources
     */
    @GetMapping
    @Operation(summary = "Get vehicles", description = "Retrieves vehicles, optionally filtered by organization.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicles found",
                    content = @Content(schema = @Schema(implementation = VehicleResource.class)))
    })
    public ResponseEntity<List<VehicleResource>> getVehicles(
            @Parameter(description = "Owning organization identifier", example = "1")
            @RequestParam(required = false) Long organizationId) {
        List<Vehicle> vehicles = organizationId == null
                ? List.of()
                : vehicleQueryService.handle(new GetAllVehiclesByOrganizationIdQuery(organizationId));
        var resources = vehicles.stream().map(VehicleResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    /**
     * Retrieves a single vehicle by its identifier.
     *
     * @param vehicleId the vehicle identifier
     * @return the vehicle resource, or a 404 error response
     */
    @GetMapping("/{vehicleId}")
    @Operation(summary = "Get vehicle by id", description = "Retrieves a vehicle by its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle found",
                    content = @Content(schema = @Schema(implementation = VehicleResource.class))),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    public ResponseEntity<?> getVehicleById(
            @Parameter(description = "Vehicle identifier", example = "1", required = true)
            @PathVariable Long vehicleId) {
        var vehicle = vehicleQueryService.handle(new GetVehicleByIdQuery(vehicleId));
        if (vehicle.isEmpty()) {
            return ErrorResponseAssembler.toErrorResponseFromApplicationError(
                    ApplicationError.notFound("Vehicle", vehicleId.toString()));
        }
        return ResponseEntity.ok(VehicleResourceFromEntityAssembler.toResourceFromEntity(vehicle.get()));
    }

    /**
     * Deletes a vehicle by its identifier.
     *
     * @param vehicleId the vehicle identifier
     * @return 204 on success, or a 404 error response
     */
    @DeleteMapping("/{vehicleId}")
    @Operation(summary = "Delete a vehicle", description = "Deletes a vehicle by its identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Vehicle deleted"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    public ResponseEntity<?> deleteVehicle(
            @Parameter(description = "Vehicle identifier", example = "1", required = true)
            @PathVariable Long vehicleId) {
        var result = vehicleCommandService.handle(new DeleteVehicleCommand(vehicleId));
        if (result.isFailure()) {
            return ErrorResponseAssembler.toErrorResponseFromApplicationError(
                    ((Result.Failure<Long, ApplicationError>) result).error());
        }
        return ResponseEntity.noContent().build();
    }
}
