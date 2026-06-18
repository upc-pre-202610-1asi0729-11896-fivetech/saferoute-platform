package com.fivetech.platform.fleet.domain.model.events;

/**
 * Domain event published when a route's daily departure time has been set.
 *
 * <p>Event Storming sticky: <b>DepartureTimeSet</b> (outcome of "Set Departure Time"). In the
 * current implementation this fact is established as part of route definition, so the event is
 * emitted together with {@link RouteDefinedEvent}.</p>
 *
 * @param routeId       identity of the route
 * @param departureTime the daily departure time in {@code HH:mm} format
 */
public record DepartureTimeSetEvent(
        Long routeId,
        String departureTime) {
}
