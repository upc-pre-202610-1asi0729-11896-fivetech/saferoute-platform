package com.acme.saferoute.platform.trip.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * Input resource for reporting an incident during a trip. The trip is taken from the URL path.
 *
 * @param description the incident description
 */
@Schema(
        name = "ReportIncidentRequest",
        description = "Request payload for reporting an incident during a trip",
        example = "{\"description\": \"Heavy traffic on Main Avenue causing a 25-minute delay\"}")
public record ReportIncidentResource(

        @NotBlank(message = "description must not be blank")
        @Schema(description = "Incident description", example = "Heavy traffic causing a delay")
        String description
) {
}
