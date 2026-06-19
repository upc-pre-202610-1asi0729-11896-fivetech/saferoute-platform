package com.fivetech.platform.fleet.domain.model.valueobjects;

/**
 * Value object representing the identifier of the organization that owns a fleet element.
 *
 * <p>The {@code organizationId} is a reference to an aggregate owned by the IAM bounded
 * context. It is intentionally modeled as a local value object (rather than a shared type)
 * so the Fleet context stays independent and does not depend on the IAM context's model.</p>
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
