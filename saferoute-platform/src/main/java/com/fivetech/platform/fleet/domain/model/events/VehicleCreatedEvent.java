package com.acme.saferoute.platform.fleet.domain.model.events;

/**
 * Domain event published when a new vehicle has been registered and persisted.
 *
 * @param vehicleId      identity assigned to the newly created vehicle
 * @param plate          vehicle license plate
 * @param organizationId identifier of the owning organization
 */
public record VehicleCreatedEvent(
        Long vehicleId,
        String plate,
        Long organizationId) {
}
