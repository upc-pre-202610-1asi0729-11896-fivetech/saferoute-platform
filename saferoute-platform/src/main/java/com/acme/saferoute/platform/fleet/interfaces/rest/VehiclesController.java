package com.acme.saferoute.platform.fleet.interfaces.rest;

import com.acme.saferoute.platform.fleet.application.commandservices.VehicleCommandService;
import com.acme.saferoute.platform.fleet.interfaces.rest.resources.VehicleResource;
import com.acme.saferoute.platform.fleet.interfaces.rest.transform.CreateVehicleCommandFromResourceAssembler;
import com.acme.saferoute.platform.fleet.interfaces.rest.transform.UpdateVehicleCommandFromResourceAssembler;
import com.acme.saferoute.platform.fleet.interfaces.rest.transform.VehicleResourceFromEntityAssembler;
import com.acme.saferoute.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
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

    /**
     * Creates the controller with the vehicle application services.
     *
     * @param vehicleCommandService the vehicle command service
     */
    public VehiclesController(VehicleCommandService vehicleCommandService) {
        this.vehicleCommandService = vehicleCommandService;
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
}
