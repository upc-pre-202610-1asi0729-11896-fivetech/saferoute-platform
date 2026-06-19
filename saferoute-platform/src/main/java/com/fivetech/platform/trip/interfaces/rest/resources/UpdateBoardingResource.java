package com.acme.saferoute.platform.trip.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Input resource for updating a child's boarding status during a trip (US-11). The trip is taken
 * from the URL path.
 *
 * @param childId       identifier of the child whose boarding state changes
 * @param boardingState the new boarding state code
 */
@Schema(
        name = "UpdateBoardingRequest",
        description = "Request payload for updating a child's boarding status",
        example = "{\"childId\": 10, \"boardingState\": \"BOARDED\"}")
public record UpdateBoardingResource(

        @NotNull(message = "childId is required")
        @Positive(message = "childId must be positive")
        @Schema(description = "Child identifier", example = "10")
        Long childId,

        @NotBlank(message = "boardingState must not be blank")
        @Schema(description = "New boarding state", example = "BOARDED",
                allowableValues = {"PENDING", "BOARDED", "ABSENT", "DROPPED_OFF"})
        String boardingState
) {
}
