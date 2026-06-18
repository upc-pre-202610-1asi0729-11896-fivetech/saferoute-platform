package com.fivetech.platform.fleet.domain.model.valueobjects;

/**
 * Value object referencing a {@code Route} aggregate from within the {@code Assignment}
 * aggregate.
 *
 * <p>An assignment links a driver and a set of children to a route; it references the route
 * by identity (this value object) rather than by object reference, respecting aggregate
 * boundaries inside the Fleet bounded context.</p>
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
