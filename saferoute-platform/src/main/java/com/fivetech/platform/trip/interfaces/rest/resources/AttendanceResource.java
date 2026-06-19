package com.acme.saferoute.platform.trip.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Output resource representing an attendance record. Field names match the frontend
 * {@code Attendance} model.
 *
 * @param id            attendance identifier
 * @param tripId        owning trip identifier
 * @param childId       tracked child identifier
 * @param boardingState boarding state (PENDING, BOARDED, ABSENT, DROPPED_OFF)
 * @param boardedAt     boarding timestamp, or null if not boarded yet
 */
@Schema(name = "AttendanceResponse", description = "Attendance information response")
public record AttendanceResource(
        @Schema(description = "Attendance identifier", example = "1")
        Long id,

        @Schema(description = "Owning trip identifier", example = "1")
        Long tripId,

        @Schema(description = "Tracked child identifier", example = "10")
        Long childId,

        @Schema(description = "Boarding state", example = "BOARDED")
        String boardingState,

        @Schema(description = "Boarding timestamp (ISO-8601), null if not boarded", example = "2026-06-09T07:35:00Z")
        Instant boardedAt
) {
}
