package com.acme.saferoute.platform.trip.domain.model.valueobjects;

/**
 * Value object referencing a child whose boarding is tracked during a trip.
 *
 * <p>The child concept is owned by the Stakeholder bounded context; Trip references it by identity
 * through this local value object inside its {@code Attendance} child entities.</p>
 *
 * @param childId the child identifier; must not be null or less than 1
 */
public record ChildId(Long childId) {

    /**
     * Compact constructor enforcing the child identifier invariant.
     *
     * @throws IllegalArgumentException if {@code childId} is null or less than 1
     */
    public ChildId {
        if (childId == null || childId < 1) {
            throw new IllegalArgumentException("ChildId must be a positive value");
        }
    }
}
