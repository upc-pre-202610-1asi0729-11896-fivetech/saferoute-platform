package com.fivetech.platform.fleet.domain.model.valueobjects;

/**
 * Enumeration representing the operational status of a {@code Vehicle}.
 *
 * <p>Mirrors the {@code status} field used by the SafeRoute clients. A vehicle is {@link #ACTIVE}
 * and usable by default, may be temporarily out of service for {@link #MAINTENANCE}, or retired as
 * {@link #INACTIVE}.</p>
 */
public enum VehicleStatus {
    /** The vehicle is operational and can be assigned to routes. Default. */
    ACTIVE,
    /** The vehicle is temporarily out of service for maintenance. */
    MAINTENANCE,
    /** The vehicle has been retired from the fleet. */
    INACTIVE;

    /**
     * Parses a textual status, applying the ACTIVE default for null/blank input.
     *
     * @param value the textual status (case-insensitive), or null
     * @return the parsed {@link VehicleStatus}
     * @throws IllegalArgumentException if {@code value} is non-blank but not a known status
     */
    public static VehicleStatus fromNullable(String value) {
        if (value == null || value.isBlank()) {
            return ACTIVE;
        }
        try {
            return VehicleStatus.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                    "Unknown vehicle status '%s'. Allowed values: ACTIVE, MAINTENANCE, INACTIVE".formatted(value));
        }
    }
}
