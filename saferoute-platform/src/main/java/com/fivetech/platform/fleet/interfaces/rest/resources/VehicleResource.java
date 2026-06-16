package com.fivetech.platform.fleet.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.jspecify.annotations.Nullable;

/**
 * Vehicle resource matching the SafeRoute (Angular) front-end contract.
 *
 * <p>Used for both request and response bodies on {@code /api/v1/vehicles}. {@code brand} is
 * optional (the front does not provide it) and omitted from the JSON when null; {@code status}
 * defaults to ACTIVE when omitted on create.</p>
 *
 * @param id             vehicle identifier (null on create)
 * @param organizationId owning organization identifier
 * @param plate          license plate
 * @param model          vehicle model
 * @param capacity       passenger capacity
 * @param status         operational status (ACTIVE / MAINTENANCE / INACTIVE)
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "Vehicle", description = "Vehicle resource (front-end compatible)")
public record VehicleResource(
        @Schema(description = "Vehicle identifier", example = "1")
        Long id,

        @NotNull(message = "organizationId is required")
        @Positive(message = "organizationId must be positive")
        @Schema(description = "Owning organization identifier", example = "1")
        Long organizationId,

        @NotBlank(message = "plate must not be blank")
        @Schema(description = "License plate", example = "ABC-123")
        String plate,

        @NotBlank(message = "model must not be blank")
        @Schema(description = "Vehicle model", example = "Mercedes Sprinter")
        String model,

        @Positive(message = "capacity must be positive")
        @Schema(description = "Passenger capacity", example = "20")
        int capacity,

        @Nullable
        @Schema(description = "Operational status", example = "ACTIVE")
        String status
) {
}
