package com.fivetech.platform.fleet.domain.model.commands;

import java.util.List;

/**
 * Command expressing the intent to update an existing route's information.
 *
 * <p>{@code serviceDays} is optional: a null value keeps the currently persisted operating days
 * unchanged. Stops are not part of this command — they are managed through the dedicated stop
 * operations.</p>
 *
 * @param routeId       identifier of the route to update
 * @param name          new route name
 * @param departureTime new daily departure time in {@code HH:mm} format
 * @param serviceDays   new operating weekday codes, or null to keep the current ones
 * @param vehicleId     new assigned vehicle identifier, or null to clear the assignment
 * @param routeType     new route direction code (OUTBOUND/INBOUND); null defaults to OUTBOUND
 */
public record UpdateRouteCommand(
        Long routeId,
        String name,
        String departureTime,
        List<String> serviceDays,
        Long vehicleId,
        String routeType) {
}
