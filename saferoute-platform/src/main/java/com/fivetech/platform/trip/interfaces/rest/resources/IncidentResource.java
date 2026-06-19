package com.acme.saferoute.platform.trip.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Output resource representing an incident. Field names match the frontend {@code Incident} model.
 *
 * @param id          incident identifier
 * @param tripId      owning trip identifier
 * @param description incident description
 * @param reportedAt  report timestamp
 */
@Schema(name = "IncidentResponse", description = "Incident information response")
public record IncidentResource(
        @Schema(description = "Incident identifier", example = "1")
        Long id,

        @Schema(description = "Owning trip identifier", example = "1")
        Long tripId,

        @Schema(description = "Incident description", example = "Heavy traffic causing a delay")
        String description,

        @Schema(description = "Report timestamp (ISO-8601)", example = "2026-06-09T07:50:00Z")
        Instant reportedAt
) {
}
