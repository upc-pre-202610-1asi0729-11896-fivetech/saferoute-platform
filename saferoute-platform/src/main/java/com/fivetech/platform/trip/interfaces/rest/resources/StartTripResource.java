package com.acme.saferoute.platform.trip.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Input resource for starting a trip (US-10). Mirrors the frontend start-trip payload.
 *
 * @param organizationId owning organization identifier
 * @param routeId        route to execute
 * @param driverId       driver starting the trip
 */
@Schema(
        name = "StartTripRequest",
        description = "Request payload for starting a trip",
        example = "{\"organizationId\": 1, \"routeId\": 1, \"driverId\": 5}")
public record StartTripResource(

        @NotNull(message = "organizationId is required")
        @Positive(message = "organizationId must be positive")
        @Schema(description = "Owning organization identifier", example = "1")
        Long organizationId,

        @NotNull(message = "routeId is required")
        @Positive(message = "routeId must be positive")
        @Schema(description = "Route to execute", example = "1")
        Long routeId,

        @NotNull(message = "driverId is required")
        @Positive(message = "driverId must be positive")
        @Schema(description = "Driver starting the trip", example = "5")
        Long driverId
) {
}
