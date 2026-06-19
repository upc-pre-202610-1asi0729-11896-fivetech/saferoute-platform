package com.fivetech.platform.fleet.domain.model.valueobjects;

/**
 * Value object representing a geographic coordinate pair (latitude / longitude) of a stop.
 *
 * <p>Encapsulates the geo-position invariants: latitude must be within [-90, 90] and longitude
 * within [-180, 180]. Mirrors the frontend {@code Coordinates} value object and the
 * {@code latitude}/{@code longitude} fields of a Stop.</p>
 *
 * @param latitude  the latitude in decimal degrees, within [-90, 90]
 * @param longitude the longitude in decimal degrees, within [-180, 180]
 */
public record Coordinates(double latitude, double longitude) {

    /**
     * Compact constructor enforcing valid geographic ranges.
     *
     * @throws IllegalArgumentException if latitude or longitude fall outside their valid ranges
     */
    public Coordinates {
        if (latitude < -90.0 || latitude > 90.0) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90 degrees");
        }
        if (longitude < -180.0 || longitude > 180.0) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180 degrees");
        }
    }
}
