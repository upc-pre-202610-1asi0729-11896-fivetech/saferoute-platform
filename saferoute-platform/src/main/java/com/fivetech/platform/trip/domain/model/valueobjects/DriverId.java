package com.acme.saferoute.platform.trip.domain.model.valueobjects;

/**
 * Value object referencing the driver operating a trip.
 *
 * <p>The driver concept is owned by the Stakeholder bounded context; Trip references it by
 * identity through this local value object.</p>
 *
 * @param driverId the driver identifier; must not be null or less than 1
 */
public record DriverId(Long driverId) {

    /**
     * Compact constructor enforcing the driver identifier invariant.
     *
     * @throws IllegalArgumentException if {@code driverId} is null or less than 1
     */
    public DriverId {
        if (driverId == null || driverId < 1) {
            throw new IllegalArgumentException("DriverId must be a positive value");
        }
    }
}
