package com.fivetech.platform.fleet.infrastructure.persistence.jpa.embeddables;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Persistence representation of the {@code Coordinates} value object.
 *
 * <p>Embedded inside the {@code stops} table as two columns. Kept in the infrastructure layer so
 * the domain {@code Coordinates} record stays free of JPA annotations.</p>
 */
@Embeddable
public class CoordinatesPersistenceEmbeddable {

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    /**
     * Default constructor required by JPA.
     */
    public CoordinatesPersistenceEmbeddable() {
    }

    /**
     * Creates the embeddable from latitude and longitude values.
     *
     * @param latitude  the latitude in decimal degrees
     * @param longitude the longitude in decimal degrees
     */
    public CoordinatesPersistenceEmbeddable(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
