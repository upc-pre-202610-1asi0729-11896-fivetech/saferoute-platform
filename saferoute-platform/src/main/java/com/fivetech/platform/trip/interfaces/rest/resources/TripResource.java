package com.acme.saferoute.platform.trip.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Output resource representing a trip. Field names and shape match the frontend {@code Trip} model
 * (timestamps serialized as ISO-8601 strings).
 *
 * @param id             trip identifier
 * @param organizationId owning organization identifier
 * @param routeId        executed route identifier
 * @param driverId       operating driver identifier
 * @param tripState      lifecycle state (IN_PROGRESS, COMPLETED, CANCELLED)
 * @param startTime      start timestamp
 * @param endTime        end timestamp, or null while in progress
 */
@Schema(name = "TripResponse", description = "Trip information response")
public record TripResource(
        @Schema(description = "Trip identifier", example = "1")
        Long id,

        @Schema(description = "Owning organization identifier", example = "1")
        Long organizationId,

        @Schema(description = "Executed route identifier", example = "1")
        Long routeId,

        @Schema(description = "Operating driver identifier", example = "5")
        Long driverId,

        @Schema(description = "Lifecycle state", example = "IN_PROGRESS")
        String tripState,

        @Schema(description = "Start timestamp (ISO-8601)", example = "2026-06-09T07:30:00Z")
        Instant startTime,

        @Schema(description = "End timestamp (ISO-8601), null while in progress", example = "2026-06-09T08:15:00Z")
        Instant endTime
) {
}
