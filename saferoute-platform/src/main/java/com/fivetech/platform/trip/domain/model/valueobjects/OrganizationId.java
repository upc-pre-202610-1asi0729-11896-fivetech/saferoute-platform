package com.acme.saferoute.platform.trip.domain.model.valueobjects;

/**
 * Value object referencing the organization a trip belongs to.
 *
 * <p>Modeled locally inside the Trip bounded context (rather than shared with IAM/Fleet) so the
 * context remains independent and free of cross-context model coupling.</p>
 *
 * @param organizationId the organization identifier; must not be null or less than 1
 */
public record OrganizationId(Long organizationId) {

    /**
     * Compact constructor enforcing the organization identifier invariant.
     *
     * @throws IllegalArgumentException if {@code organizationId} is null or less than 1
     */
    public OrganizationId {
        if (organizationId == null || organizationId < 1) {
            throw new IllegalArgumentException("OrganizationId must be a positive value");
        }
    }
}
