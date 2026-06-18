package com.fivetech.platform.fleet.domain.model.commands;

/**
 *
 * @param name      stop name
 * @param latitude  stop latitude in decimal degrees
 * @param longitude stop longitude in decimal degrees
 * @param order     1-based position within the route sequence
 */
public record RouteStopData(
        String name,
        double latitude,
        double longitude,
        int order) {
}
