package com.fivetech.platform.fleet.domain.model.commands;

/**
 * Command expressing the intent to delete a vehicle by its identifier.
 *
 * @param vehicleId identifier of the vehicle to delete
 */
public record DeleteVehicleCommand(Long vehicleId) {
}
