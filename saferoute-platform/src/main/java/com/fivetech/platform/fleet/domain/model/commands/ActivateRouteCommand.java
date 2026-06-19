package com.fivetech.platform.fleet.domain.model.commands;

/**
 * Command expressing the intent to activate a route (US-6: transition DRAFT to ACTIVE).
 *
 * @param routeId identifier of the route to activate
 */
public record ActivateRouteCommand(Long routeId) {
}
