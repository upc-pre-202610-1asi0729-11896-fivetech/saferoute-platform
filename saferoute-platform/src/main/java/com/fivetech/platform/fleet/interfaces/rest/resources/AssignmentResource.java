package com.acme.saferoute.platform.fleet.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Output resource representing an assignment. Field names match the frontend {@code Assignment}
 * model.
 *
 * @param id       assignment identifier
 * @param routeId  staffed route identifier
 * @param driverId assigned driver identifier
 * @param childIds assigned children identifiers
 */
@Schema(name = "AssignmentResponse", description = "Assignment information response")
public record AssignmentResource(
        @Schema(description = "Assignment identifier", example = "1")
        Long id,

        @Schema(description = "Staffed route identifier", example = "1")
        Long routeId,

        @Schema(description = "Assigned driver identifier", example = "5")
        Long driverId,

        @Schema(description = "Assigned children identifiers", example = "[10, 11, 12]")
        List<Long> childIds
) {
}
