package com.acme.saferoute.platform.fleet.domain.model.events;

import java.util.List;

/**
 * Domain event published when students (children) have been assigned to a route through its
 * staffing assignment.
 *
 * <p>Event Storming sticky: <b>StudentAssignedToRoute</b> (outcome of "Assign Students to Route";
 * precedes "Generate Passenger Manifest"). Also carries the assigned driver, since the Assignment
 * aggregate staffs both driver and children. The Notifications context can subscribe to alert the
 * driver (US-6 S1).</p>
 *
 * @param assignmentId identity of the assignment
 * @param routeId      identity of the staffed route
 * @param driverId     identity of the assigned driver
 * @param childIds     identities of the assigned children
 */
public record StudentAssignedToRouteEvent(
        Long assignmentId,
        Long routeId,
        Long driverId,
        List<Long> childIds) {
}
