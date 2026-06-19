package com.fivetech.platform.fleet.domain.model.commands;

/**
 * Command expressing the intent to delete a route by its identifier.
 *
 * @param routeId identifier of the route to delete
 */
public record DeleteRouteCommand(Long routeId) {
}
