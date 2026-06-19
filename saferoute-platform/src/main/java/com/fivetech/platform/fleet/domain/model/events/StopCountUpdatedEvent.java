package com.fivetech.platform.fleet.domain.model.events;

/**
 * Domain event published when a route's stop sequence changes, reflecting the new stop count.
 *
 * <p>Event Storming sticky: <b>StopCountUpdated</b> (follows "Pick Waypoints" / "Recalculate
 * Stops"). Emitted when a route is defined or updated with a non-empty set of stops.</p>
 *
 * @param routeId   identity of the route
 * @param stopCount the resulting number of stops on the route
 */
public record StopCountUpdatedEvent(
        Long routeId,
        int stopCount) {
}
