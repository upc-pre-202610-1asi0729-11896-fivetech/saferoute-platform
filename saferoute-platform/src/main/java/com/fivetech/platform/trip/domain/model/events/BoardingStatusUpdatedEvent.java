package com.acme.saferoute.platform.trip.domain.model.events;

/**
 * Domain event published when a child's boarding status changes during a trip (US-11).
 *
 * <p>Notably, a transition to {@code BOARDED} allows the Notifications context to inform the
 * corresponding parent that their child has been picked up.</p>
 *
 * @param tripId        identity of the trip
 * @param childId       identifier of the child
 * @param boardingState the new boarding state name
 */
public record BoardingStatusUpdatedEvent(
        Long tripId,
        Long childId,
        String boardingState) {
}
