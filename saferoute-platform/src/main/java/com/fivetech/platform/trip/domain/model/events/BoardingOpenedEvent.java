package com.acme.saferoute.platform.trip.domain.model.events;

/**
 * Domain event published when boarding is opened for a trip.
 *
 * <p>Event Storming sticky: <b>BoardingOpened</b> (outcome of "Initialize Boarding" / "Open
 * Boarding"). In the current implementation boarding opens as soon as the trip starts, so this
 * event is emitted together with {@link TripStartedEvent}.</p>
 *
 * @param tripId identity of the trip whose boarding was opened
 */
public record BoardingOpenedEvent(Long tripId) {
}
