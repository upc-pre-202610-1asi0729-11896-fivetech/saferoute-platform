package com.acme.saferoute.platform.trip.domain.model.valueobjects;

/**
 * Enumeration representing the lifecycle state of a {@code Trip}.
 *
 * <p>Drives the trip execution flow (US-10 start, US-14 finish): a trip begins {@link #IN_PROGRESS}
 * when the driver starts the route and becomes {@link #COMPLETED} once the route is closed. A trip
 * may also be {@link #CANCELLED} when aborted before completion.</p>
 */
public enum TripState {
    /**
     * The trip is currently underway; coordinates and boarding are being tracked. Initial state.
     */
    IN_PROGRESS,

    /**
     * The trip has been finished by the driver.
     */
    COMPLETED,

    /**
     * The trip was aborted before completion.
     */
    CANCELLED
}
