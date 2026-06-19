package com.acme.saferoute.platform.trip.domain.model.queries;

/**
 * Query to retrieve the attendance records of a trip.
 *
 * <p>Backs the frontend {@code getAttendancesByTrip(tripId)} call.</p>
 *
 * @param tripId the trip identifier
 */
public record GetAttendancesByTripIdQuery(Long tripId) {
}
