package com.fivetech.platform.fleet.domain.model.events;

/**
 * Domain event published when a vehicle has been assigned to a route.
 *
 * <p>Event Storming sticky: <b>VehicleAssignedToRoute</b> (outcome of "Select Vehicle"). Emitted
 * when a route is defined or updated carrying a vehicle.</p>
 *
 * @param routeId   identity of the route
 * @param vehicleId identity of the assigned vehicle
 */
public record VehicleAssignedToRouteEvent(
        Long routeId,
        Long vehicleId) {
}
