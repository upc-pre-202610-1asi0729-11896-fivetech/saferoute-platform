package com.acme.saferoute.platform.trip.domain.model.queries;

/**
 * Query to retrieve the incidents reported on a trip.
 *
 * <p>Backs the frontend {@code getIncidentsByTrip(tripId)} call.</p>
 *
 * @param tripId the trip identifier
 */
public record GetIncidentsByTripIdQuery(Long tripId) {
}
