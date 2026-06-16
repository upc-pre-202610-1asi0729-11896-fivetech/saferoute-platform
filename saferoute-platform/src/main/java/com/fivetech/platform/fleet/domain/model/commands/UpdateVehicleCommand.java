package com.acme.saferoute.platform.fleet.domain.model.commands;

/**
 * Command expressing the intent to update an existing vehicle's information.
 *
 * @param vehicleId identifier of the vehicle to update
 * @param plate     vehicle license plate
 * @param model     vehicle model
 * @param capacity  passenger capacity; must be positive
 * @param status    operational status code (ACTIVE/MAINTENANCE/INACTIVE); null defaults to ACTIVE
 */
public record UpdateVehicleCommand(
        Long vehicleId,
        String plate,
        String model,
        int capacity,
        String status) {
}
