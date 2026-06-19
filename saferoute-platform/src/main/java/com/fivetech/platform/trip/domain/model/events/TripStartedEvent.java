package com.acme.saferoute.platform.trip.domain.model.events;

import java.time.Instant;

/**
 * Domain event published when a trip is started (US-10).
 *
 * <p>Enables the Notifications context to alert parents that the vehicle is on its way
 * ("En camino"), without coupling notification logic to the Trip context.</p>
 *
 * @param tripId    identity of the started trip
 * @param routeId   identifier of the executed route
 * @param driverId  identifier of the driver
 * @param startedAt timestamp at which the trip started
 */
public record TripStartedEvent(
        Long tripId,
        Long routeId,
        Long driverId,
        Instant startedAt) {
}
