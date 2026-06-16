package com.fivetech.platform.fleet.domain.model.valueobjects;

/**
 * Value object referencing a {@code Vehicle} aggregate from within other Fleet aggregates
 * (for example, the vehicle assigned to a {@code Route}).
 *
 * <p>Modeled as a value object so a route can hold a typed reference to its vehicle without
 * creating an object reference between aggregate roots, keeping aggregate boundaries clean.</p>
 *
 * @param vehicleId the vehicle identifier; must not be null or less than 1
 */
public record VehicleId(Long vehicleId) {

    /**
     * Compact constructor enforcing the vehicle identifier invariant.
     *
     * @throws IllegalArgumentException if {@code vehicleId} is null or less than 1
     */
    public VehicleId {
        if (vehicleId == null || vehicleId < 1) {
            throw new IllegalArgumentException("VehicleId must be a positive value");
        }
    }
}
