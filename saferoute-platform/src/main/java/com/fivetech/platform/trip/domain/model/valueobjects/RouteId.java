package com.acme.saferoute.platform.trip.domain.model.valueobjects;

/**
 * Value object referencing the route a trip executes.
 *
 * <p>The route is owned by the Fleet bounded context; Trip references it by identity through this
 * local value object, keeping the two contexts decoupled.</p>
 *
 * @param routeId the route identifier; must not be null or less than 1
 */
public record RouteId(Long routeId) {

    /**
     * Compact constructor enforcing the route identifier invariant.
     *
     * @throws IllegalArgumentException if {@code routeId} is null or less than 1
     */
    public RouteId {
        if (routeId == null || routeId < 1) {
            throw new IllegalArgumentException("RouteId must be a positive value");
        }
    }
}
