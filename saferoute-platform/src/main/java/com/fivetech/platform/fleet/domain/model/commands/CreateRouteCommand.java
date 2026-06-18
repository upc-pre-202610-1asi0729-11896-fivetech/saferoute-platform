package com.fivetech.platform.fleet.domain.model.commands;

import java.util.List;

/**
 * Command expressing the intent to create a new route (US-5).
 *
 * <p>Carries primitive, transport-friendly values; value object construction and invariant
 * enforcement happen inside the {@code Route} aggregate. Stops are NOT part of route creation —
 * they are managed separately through the dedicated stop operations (addStop / replaceStops).</p>
 *
 * @param name           human-readable route name
 * @param organizationId identifier of the owning organization
 * @param departureTime  daily departure time in {@code HH:mm} format
 * @param serviceDays    weekday codes the route operates on
 * @param vehicleId      identifier of the assigned vehicle, or {@code null} if not yet assigned
 * @param routeType      route direction code (OUTBOUND/INBOUND); null defaults to OUTBOUND
 */
public record CreateRouteCommand(
        String name,
        Long organizationId,
        String departureTime,
        List<String> serviceDays,
        Long vehicleId,
        String routeType) {
}
