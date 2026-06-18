package com.fivetech.platform.fleet.domain.model.valueobjects;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Value object representing the daily departure time of a {@code Route}.
 *
 * <p>Wraps a {@link LocalTime} to guarantee a valid time-of-day while preserving the
 * {@code "HH:mm"} string contract used by the SafeRoute frontend. Construction accepts the
 * frontend string form and rejects malformed values, keeping the invariant inside the model.</p>
 *
 * @param value the time of day at which the route departs; never null
 */
public record DepartureTime(LocalTime value) {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Compact constructor enforcing a non-null time.
     *
     * @throws IllegalArgumentException if {@code value} is null
     */
    public DepartureTime {
        if (value == null) {
            throw new IllegalArgumentException("DepartureTime must not be null");
        }
    }

    /**
     * Factory that builds a {@link DepartureTime} from the frontend {@code "HH:mm"} string.
     *
     * @param text the textual time in {@code HH:mm} format (e.g. "07:30")
     * @return the parsed {@link DepartureTime}
     * @throws IllegalArgumentException if {@code text} is null, blank or not a valid {@code HH:mm} time
     */
    public static DepartureTime of(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("DepartureTime must not be null or blank");
        }
        try {
            return new DepartureTime(LocalTime.parse(text.trim(), FORMATTER));
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("DepartureTime must follow the HH:mm format", exception);
        }
    }

    /**
     * Renders this departure time using the {@code "HH:mm"} contract expected by the frontend.
     *
     * @return the formatted time string
     */
    public String asText() {
        return value.format(FORMATTER);
    }
}
