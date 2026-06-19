package com.fivetech.platform.fleet.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * Route resource matching the SafeRoute (Angular) front-end contract.
 *
 * <p>Flat compatibility resource for both request and response bodies on {@code /api/v1/routes}.
 * It denormalizes route data plus the staffing assignment (driver / students) and the assigned
 * vehicle's plate. The route's <b>stops</b> are NOT nested here: following the project design
 * (and Learning's flat-resource convention), stops are exposed as their own {@code StopResource}
 * through the dedicated {@code /api/v1/routes/{id}/stops} endpoints.</p>
 *
 * @param id                 route identifier (null on create)
 * @param name               route name
 * @param type               route direction (e.g. OUTBOUND / INBOUND)
 * @param status             lifecycle status (ACTIVE / DRAFT / INACTIVE)
 * @param driverId           assigned driver id, or null
 * @param driverName         assigned driver display name (read-only, may be empty)
 * @param vehicleId          assigned vehicle id, or null
 * @param vehiclePlate       assigned vehicle plate (read-only, may be empty)
 * @param studentIds         ids of children transported on the route
 * @param scheduledStartTime daily departure time in HH:mm format
 * @param organizationId     owning organization id
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "Route", description = "Route resource (front-end compatible, flat shape)")
public record RouteResource(
        @Schema(description = "Route identifier", example = "1")
        Long id,

        @NotBlank(message = "name must not be blank")
        @Schema(description = "Route name", example = "Ruta Norte — Comas / Los Olivos")
        String name,

        @Nullable
        @Schema(description = "Route direction", example = "OUTBOUND")
        String type,

        @Nullable
        @Schema(description = "Lifecycle status", example = "ACTIVE")
        String status,

        @Nullable
        @Schema(description = "Assigned driver id", example = "2")
        Long driverId,

        @Nullable
        @Schema(description = "Assigned driver display name (read-only)", example = "Carlos Ramirez")
        String driverName,

        @Nullable
        @Schema(description = "Assigned vehicle id", example = "1")
        Long vehicleId,

        @Nullable
        @Schema(description = "Assigned vehicle plate (read-only)", example = "ABC-123")
        String vehiclePlate,

        @Nullable
        @Schema(description = "Ids of children transported", example = "[1, 2, 5]")
        List<Long> studentIds,

        @Nullable
        @Schema(description = "Daily departure time in HH:mm", example = "06:00")
        String scheduledStartTime,

        @NotNull(message = "organizationId is required")
        @Positive(message = "organizationId must be positive")
        @Schema(description = "Owning organization id", example = "1")
        Long organizationId
) {
}
