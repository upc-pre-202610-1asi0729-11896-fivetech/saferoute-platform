package com.acme.saferoute.platform.fleet.domain.model.commands;

import java.util.List;

/**
 * Command expressing the intent to replace the staffing (driver and children) of the assignment
 * attached to a route (US-6 reassignment flow).
 *
 * @param routeId  identifier of the route whose assignment is updated
 * @param driverId identifier of the new driver
 * @param childIds replacement children identifiers (may be empty)
 */
public record UpdateAssignmentCommand(
        Long routeId,
        Long driverId,
        List<Long> childIds) {
}
