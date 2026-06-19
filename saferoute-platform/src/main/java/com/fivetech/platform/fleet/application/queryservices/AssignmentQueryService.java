package com.acme.saferoute.platform.fleet.application.queryservices;

import com.acme.saferoute.platform.fleet.domain.model.aggregates.Assignment;
import com.acme.saferoute.platform.fleet.domain.model.queries.GetAssignmentByRouteIdQuery;

import java.util.Optional;

/**
 * Application service contract for Assignment read operations within the Fleet bounded context.
 */
public interface AssignmentQueryService {

    /**
     * Resolves the assignment for a given route.
     *
     * @param query the {@link GetAssignmentByRouteIdQuery}
     * @return the assignment if found, otherwise empty
     */
    Optional<Assignment> handle(GetAssignmentByRouteIdQuery query);
}
