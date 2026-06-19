package com.acme.saferoute.platform.fleet.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * Input resource for adding a stop to a route (PUML: {@code addStop}). The owning route comes from
 * the URL path. Also used as the element type when replacing a route's whole stop sequence.
 *
 * @param name      stop name
 * @param latitude  latitude in decimal degrees
 * @param longitude longitude in decimal degrees
 * @param stopOrder 1-based position within the route sequence
 */
@Schema(name = "AddStopRequest", description = "Request payload for a route stop")
public record AddStopResource(
        @NotBlank(message = "name must not be blank")
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
