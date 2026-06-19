package com.fivetech.platform.fleet.domain.model.valueobjects;

/**
 * Enumeration representing the lifecycle state of a {@code Route}.
 *
 * <p>Drives the route activation flow (US-5 / US-6): a route is created as {@link #DRAFT},
 * is published for operation as {@link #ACTIVE}, and can be retired as {@link #INACTIVE}.
 * The frontend persists this as the {@code routeState} string field.</p>
 */
public enum RouteState {
    /**
     * The route has been created but is not yet operational. Initial state.
     */
    DRAFT,

    /**
     * The route is operational and can be used to start trips.
     */
    ACTIVE,

    /**
     * The route has been retired and is no longer operational.
     */
    INACTIVE
}
