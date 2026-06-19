package com.acme.saferoute.platform.fleet.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

/**
 * Input resource for assigning a driver and children to a route (US-6). Mirrors the frontend
 * assignment creation payload.
 *
 * @param routeId  staffed route identifier
 * @param driverId assigned driver identifier
 * @param childIds assigned children identifiers
 */
@Schema(
        name = "CreateAssignmentRequest",
        description = "Request payload for assigning a driver and children to a route",
        example = "{\"routeId\": 1, \"driverId\": 5, \"childIds\": [10, 11, 12]}")
public record CreateAssignmentResource(

        @NotNull(message = "routeId is required")
        @Positive(message = "routeId must be positive")
        @Schema(description = "Staffed route identifier", example = "1")
        Long routeId,

        @NotNull(message = "driverId is required")
        @Positive(message = "driverId must be positive")
        @Schema(description = "Assigned driver identifier", example = "5")
        Long driverId,

        @NotEmpty(message = "childIds must contain at least one child")
        @Schema(description = "Assigned children identifiers", example = "[10, 11, 12]")
        List<Long> childIds
) {
}
