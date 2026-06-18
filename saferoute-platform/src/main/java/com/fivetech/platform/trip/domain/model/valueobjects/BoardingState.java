package com.acme.saferoute.platform.trip.domain.model.valueobjects;

/**
 * Enumeration representing the boarding state of a child during a trip (US-11 / US-20).
 *
 * <p>Tracks each child's presence: initially {@link #PENDING}, then either {@link #BOARDED} when the
 * child gets on the vehicle, {@link #ABSENT} when the child does not show up, or {@link #DROPPED_OFF}
 * once the child has been delivered at the destination.</p>
 */
public enum BoardingState {
    /**
     * The child has not yet boarded and is awaited at a stop. Default state.
     */
    PENDING,

    /**
     * The child has boarded the vehicle.
     */
    BOARDED,

    /**
     * The child did not show up at the stop.
     */
    ABSENT,

    /**
     * The child has been delivered at the destination.
     */
    DROPPED_OFF
}
