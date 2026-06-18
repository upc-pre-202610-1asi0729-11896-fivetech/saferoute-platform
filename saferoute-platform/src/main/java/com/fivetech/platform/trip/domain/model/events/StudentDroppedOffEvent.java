package com.acme.saferoute.platform.trip.domain.model.events;

/**
 * Domain event published when a child is dropped off at the destination during a trip.
 *
 * <p>Event Storming sticky: <b>StudentDroppedOff</b> (outcome of "Set Boarding Status" →
 * DROPPED_OFF). The Notifications context can subscribe to confirm delivery to the parent (US-20).</p>
 *
 * @param tripId  identity of the trip
 * @param childId identity of the dropped-off child
 */
public record StudentDroppedOffEvent(
        Long tripId,
        Long childId) {
}
