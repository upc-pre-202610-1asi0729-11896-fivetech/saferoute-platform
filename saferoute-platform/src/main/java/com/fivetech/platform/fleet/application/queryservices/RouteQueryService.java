package com.fivetech.platform.fleet.application.queryservices;

import com.fivetech.platform.fleet.domain.model.aggregates.Route;
import com.fivetech.platform.fleet.domain.model.entities.Stop;
import com.fivetech.platform.fleet.domain.model.queries.GetAllRoutesByOrganizationIdQuery;
import com.fivetech.platform.fleet.domain.model.queries.GetRouteByIdQuery;
import com.fivetech.platform.fleet.domain.model.queries.GetStopsByRouteIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Application service contract for Route read operations within the Fleet bounded context.
 */
public interface RouteQueryService {

    /**
     * Resolves a route by its identity.
     *
     * @param query the {@link GetRouteByIdQuery}
     * @return the route if found, otherwise empty
     */
    Optional<Route> handle(GetRouteByIdQuery query);

    /**
     * Resolves all routes for an organization.
     *
     * @param query the {@link GetAllRoutesByOrganizationIdQuery}
     * @return the matching routes (possibly empty)
     */
    List<Route> handle(GetAllRoutesByOrganizationIdQuery query);

    /**
     * Resolves the ordered stops belonging to a route.
     *
     * @param query the {@link GetStopsByRouteIdQuery}
     * @return the route's stops (possibly empty)
     */
    List<Stop> handle(GetStopsByRouteIdQuery query);
}
