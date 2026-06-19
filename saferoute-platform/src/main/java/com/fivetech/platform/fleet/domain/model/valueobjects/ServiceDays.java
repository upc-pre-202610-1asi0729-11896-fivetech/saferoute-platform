package com.fivetech.platform.fleet.domain.model.valueobjects;

import java.util.List;

/**
 * Value object representing the set of weekdays a {@code Route} operates on.
 *
 * <p>Mirrors the frontend {@code serviceDays: string[]} contract (e.g. {@code ["MON","TUE"]}).
 * The list is normalized to be non-null and immutable, and at least one service day is required,
 * which encodes the business rule that a route must operate on some day.</p>
 *
 * @param days the immutable list of service day codes; never null nor empty
 */
public record ServiceDays(List<String> days) {

    /**
     * Compact constructor that validates and defensively copies the service day codes.
     *
     * @throws IllegalArgumentException if {@code days} is null, empty, or contains blank entries
     */
    public ServiceDays {
        if (days == null || days.isEmpty()) {
            throw new IllegalArgumentException("A route must define at least one service day");
        }
        if (days.stream().anyMatch(day -> day == null || day.isBlank())) {
            throw new IllegalArgumentException("Service day codes must not be blank");
        }
        days = List.copyOf(days);
    }
}
