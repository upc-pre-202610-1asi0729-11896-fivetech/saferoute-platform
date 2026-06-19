package com.acme.saferoute.platform.fleet.domain.model.queries;

/**
 * Query to retrieve the assignment (driver + children) for a given route.
 *
 * <p>Backs the frontend {@code getAssignmentByRoute(routeId)} call.</p>
 *
 * @param routeId the route identifier
 */
public record GetAssignmentByRouteIdQuery(Long routeId) {
}
