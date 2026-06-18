package com.acme.saferoute.platform.trip.domain.model.events;

/**
 * Domain event published when a child boards the vehicle during a trip.
 *
 * <p>Event Storming sticky: <b>StudentBoarded</b> (outcome of "Set Boarding Status" → BOARDED).
 * The Notifications context can subscribe to inform the parent that their child was picked up.</p>
 *
 * @param tripId  identity of the trip
 * @param childId identity of the boarded child
 */
public record StudentBoardedEvent(
        Long tripId,
        Long childId) {
}
