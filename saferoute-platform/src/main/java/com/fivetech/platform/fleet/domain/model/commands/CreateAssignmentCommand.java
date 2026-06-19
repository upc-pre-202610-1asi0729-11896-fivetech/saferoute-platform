package com.acme.saferoute.platform.fleet.domain.model.commands;

import java.util.List;

/**
 * Command expressing the intent to assign a driver and a set of children to a route
 * (US-6: "Asignación de Roles").
 *
 * @param routeId   identifier of the route being staffed
 * @param driverId  identifier of the driver assigned to the route
 * @param childIds  identifiers of the children transported on the route
 */
public record CreateAssignmentCommand(
        Long routeId,
        Long driverId,
        List<Long> childIds) {
}
