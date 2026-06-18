package com.fivetech.platform.fleet.domain.model.events;

/**
 * Domain event published when a route's setup is complete and it has been activated.
 *
 * <p>Event Storming sticky: <b>RouteActivationFinalized</b> (outcome of the "When route setup
 * complete" policy / activation). Signals the route is operational and eligible to start trips.</p>
 *
 * @param routeId identity of the activated route
 */
public record RouteActivationFinalizedEvent(Long routeId) {
}
