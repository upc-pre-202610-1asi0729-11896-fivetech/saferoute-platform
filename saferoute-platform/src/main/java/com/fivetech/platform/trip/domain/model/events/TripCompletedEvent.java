package com.acme.saferoute.platform.trip.domain.model.events;

import java.time.Instant;

/**
 * Domain event published when a trip is completed (US-14 / US-20).
 *
 * <p>Allows the Notifications context to deliver an arrival confirmation to parents
 * ("Confirmación de Llegada").</p>
 *
 * @param tripId      identity of the completed trip
 * @param routeId     identifier of the executed route
 * @param completedAt timestamp at which the trip was completed
 */
public record TripCompletedEvent(
        Long tripId,
        Long routeId,
        Instant completedAt) {
}
