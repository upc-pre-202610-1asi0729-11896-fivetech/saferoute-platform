package com.fivetech.platform.fleet.domain.model.events;

/**
 * Domain event published when a route has been defined and persisted.
 *
 * <p>Event Storming sticky: <b>RouteDefined</b> (outcome of the "Define Route" command on the
 * {@code Route} aggregate). Other contexts (e.g. Notifications) may subscribe to react to route
 * creation without coupling to the Fleet application services.</p>
 *
 * @param routeId        identity assigned to the newly defined route
 * @param name           route name
 * @param organizationId identifier of the owning organization
 */
public record RouteDefinedEvent(
        Long routeId,
        String name,
        Long organizationId) {
}
