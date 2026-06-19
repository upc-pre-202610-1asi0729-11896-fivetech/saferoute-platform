package com.acme.saferoute.platform.trip.domain.model.queries;

/**
 * Query to retrieve all trips owned by a given organization.
 *
 * <p>Backs the trip list views (e.g. trip-list, active-trip "my trips"). Callers may further filter
 * the returned trips by driver client-side, since each trip carries its driver identity.</p>
 *
 * @param organizationId the owning organization identifier
 */
public record GetAllTripsByOrganizationIdQuery(Long organizationId) {
}
