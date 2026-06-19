package com.fivetech.platform.fleet.domain.model.queries;

/**
 * Query to retrieve the ordered list of stops belonging to a route.
 *
 * <p>Backs the frontend {@code getStopsByRoute(routeId)} call.</p>
 *
 * @param routeId the route identifier
 */
public record GetStopsByRouteIdQuery(Long routeId) {
}
