package com.acme.saferoute.platform.fleet.domain.model.queries;

/**
 * Query to retrieve all vehicles owned by a given organization.
 *
 * <p>Backs the frontend {@code getVehiclesByOrganization(organizationId)} call.</p>
 *
 * @param organizationId the owning organization identifier
 */
public record GetAllVehiclesByOrganizationIdQuery(Long organizationId) {
}
