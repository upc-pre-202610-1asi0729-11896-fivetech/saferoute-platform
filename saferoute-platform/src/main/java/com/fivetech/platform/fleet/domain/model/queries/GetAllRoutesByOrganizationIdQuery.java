package com.fivetech.platform.fleet.domain.model.queries;

/**
 * Query to retrieve all routes owned by a given organization.
 *
 * <p>Backs the frontend {@code getRoutesByOrganization(organizationId)} call.</p>
 *
 * @param organizationId the owning organization identifier
 */
public record GetAllRoutesByOrganizationIdQuery(Long organizationId) {
}
