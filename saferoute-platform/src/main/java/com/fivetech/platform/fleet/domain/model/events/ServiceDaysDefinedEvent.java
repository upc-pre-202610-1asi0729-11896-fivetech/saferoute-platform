package com.fivetech.platform.fleet.domain.model.events;

import java.util.List;

/**
 * Domain event published when a route's operating service days have been defined.
 *
 * <p>Event Storming sticky: <b>ServiceDaysDefined</b> (outcome of "Define Service Days" on the
 * {@code Route} aggregate). In the current implementation this fact is established as part of route
 * definition, so the event is emitted together with {@link RouteDefinedEvent}.</p>
 *
 * @param routeId     identity of the route
 * @param serviceDays the weekday codes the route operates on
 */
public record ServiceDaysDefinedEvent(
        Long routeId,
        List<String> serviceDays) {
}
