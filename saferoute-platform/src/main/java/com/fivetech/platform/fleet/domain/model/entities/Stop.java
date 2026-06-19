package com.fivetech.platform.fleet.domain.model.entities;
import com.fivetech.platform.fleet.domain.model.valueobjects.Coordinates;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * Stop domain entity.
 *
 * <p>Represents a single boarding point within a {@code Route}'s ordered sequence. A stop is a
 * child entity of the Route aggregate: it has its own identity but its lifecycle is governed by
 * the route, which is the consistency boundary. Geographic position is encapsulated in the
 * {@link Coordinates} value object.</p>
 */
@Getter
public class Stop {

    /**
     * Persistence identity of the stop. Assigned by the infrastructure layer; null until persisted.
     */
    @Setter
    private Long id;

    /**
     * Human-readable name of the stop (e.g. "Main Gate", "Park Avenue").
     */
    private String name;

    /**
     * Geographic position of the stop.
     */
    private Coordinates coordinates;

    /**
     * 1-based position of the stop within the route's ordered sequence.
     */
    @Setter
    private int stopOrder;

    /**
     * Full constructor used when reconstructing a stop from persistence.
     *
     * @param id          persistence identity
     * @param name        stop name
     * @param coordinates geographic position
     * @param stopOrder   position within the route sequence
     */
    public Stop(Long id, String name, Coordinates coordinates, int stopOrder) {
        this.id = id;
        this.name = validateName(name);
        this.coordinates = Objects.requireNonNull(coordinates, "coordinates must not be null");
        this.stopOrder = stopOrder;
    }

    /**
     * Domain constructor used when adding a brand-new stop to a route.
     *
     * @param name        stop name
     * @param coordinates geographic position
     * @param stopOrder   position within the route sequence
     */
    public Stop(String name, Coordinates coordinates, int stopOrder) {
        this(null, name, coordinates, stopOrder);
    }

    /**
     * Updates the stop name, enforcing the non-blank invariant.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = validateName(name);
    }

    /**
     * Updates the stop coordinates.
     *
     * @param coordinates the new geographic position
     */
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = Objects.requireNonNull(coordinates, "coordinates must not be null");
    }

    private static String validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Stop name must not be null or blank");
        }
        return name;
    }
}
