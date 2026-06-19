package com.fivetech.platform.fleet.domain.model.queries;

/**
 * Query to retrieve a single route by its unique identifier.
 *
 * @param routeId the route identifier
 */
public record GetRouteByIdQuery(Long routeId) {
}
