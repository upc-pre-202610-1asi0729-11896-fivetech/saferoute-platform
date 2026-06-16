package com.acme.saferoute.platform.fleet.domain.model.commands;

/**
 * Command expressing the intent to register a new vehicle in an organization's fleet.
 *
 * @param organizationId identifier of the owning organization
 * @param plate          vehicle license plate (unique within the fleet)
 * @param model          vehicle model
 * @param capacity       passenger capacity; must be positive
 * @param status         operational status code (ACTIVE/MAINTENANCE/INACTIVE); null defaults to ACTIVE
 */
public record CreateVehicleCommand(
        Long organizationId,
        String plate,
        String model,
        int capacity,
        String status) {
}
