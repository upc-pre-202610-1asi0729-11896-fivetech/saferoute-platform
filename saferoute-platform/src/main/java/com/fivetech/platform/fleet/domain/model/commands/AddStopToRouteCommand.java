package com.fivetech.platform.fleet.domain.model.commands;

/**
 * Command expressing the intent to add a stop to an existing route (US-5 S2).
 *
 * <p>The stop is created and ordered through the owning {@code Route} aggregate so that the
 * route remains the consistency boundary for its ordered list of stops.</p>
 *
 * @param routeId   identifier of the route the stop belongs to
 * @param name      human-readable stop name
 * @param latitude  stop latitude in decimal degrees
 * @param longitude stop longitude in decimal degrees
 * @param stopOrder 1-based position of the stop within the route sequence
 */
public record AddStopToRouteCommand(
        Long routeId,
        String name,
        double latitude,
        double longitude,
        int stopOrder) {
}
