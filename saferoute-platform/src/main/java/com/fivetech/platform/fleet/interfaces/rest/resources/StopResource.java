package com.fivetech.platform.fleet.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Output resource representing a route stop. Mirrors the {@code Stop} domain entity and the
 * project's PUML Stop shape, exposed through the dedicated {@code /routes/{id}/stops} endpoints.
 *
 * @param id        stop identifier
 * @param routeId   identifier of the owning route
 * @param name      stop name
 * @param latitude  latitude in decimal degrees
 * @param longitude longitude in decimal degrees
 * @param stopOrder 1-based position within the route sequence
 */
@Schema(name = "StopResponse", description = "Route stop information")
public record StopResource(
        @Schema(description = "Stop identifier", example = "1")
        Long id,

        @Schema(description = "Owning route identifier", example = "1")
        Long routeId,

        @Schema(description = "Stop name", example = "Av. Universitaria cdra. 54")
        String name,

        @Schema(description = "Latitude in decimal degrees", example = "-11.9553")
        double latitude,

        @Schema(description = "Longitude in decimal degrees", example = "-77.0602")
        double longitude,

        @Schema(description = "1-based position within the route sequence", example = "1")
        int stopOrder
) {
}
