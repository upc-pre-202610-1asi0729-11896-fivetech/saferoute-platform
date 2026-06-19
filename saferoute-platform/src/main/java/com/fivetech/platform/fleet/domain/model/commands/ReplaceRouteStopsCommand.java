package com.fivetech.platform.fleet.domain.model.commands;

import java.util.List;

/**
 * Command expressing the intent to replace a route's entire stop sequence in one operation.
 *
 * <p>Supports the edit flow (the client sends the full desired set of stops). The route remains the
 * consistency boundary for its stops.</p>
 *
 * @param routeId identifier of the route whose stops are replaced
 * @param stops   the full replacement stop list, in the desired order
 */
public record ReplaceRouteStopsCommand(
        Long routeId,
        List<RouteStopData> stops) {
}
