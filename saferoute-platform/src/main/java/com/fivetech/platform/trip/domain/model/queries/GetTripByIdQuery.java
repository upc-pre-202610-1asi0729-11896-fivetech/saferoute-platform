package com.acme.saferoute.platform.trip.domain.model.queries;

/**
 * Query to retrieve a single trip by its unique identifier.
 *
 * @param tripId the trip identifier
 */
public record GetTripByIdQuery(Long tripId) {
}
