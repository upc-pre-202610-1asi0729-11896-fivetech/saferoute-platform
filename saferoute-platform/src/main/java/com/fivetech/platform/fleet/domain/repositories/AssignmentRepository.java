package com.acme.saferoute.platform.fleet.domain.repositories;

import com.acme.saferoute.platform.fleet.domain.model.aggregates.Assignment;
import com.acme.saferoute.platform.fleet.domain.model.valueobjects.RouteId;

import java.util.Optional;

/**
 * Assignment repository port.
 *
 * <p>Domain-facing abstraction for persisting and retrieving {@link Assignment} aggregates.
 * A route is expected to have at most one active assignment, hence the by-route lookup returns
 * an {@link Optional}.</p>
 */
public interface AssignmentRepository {

    /**
     * Persists the given assignment (insert or update) and returns the saved aggregate.
     *
     * @param assignment the assignment to save
     * @return the persisted assignment, carrying its assigned identity
     */
    Assignment save(Assignment assignment);

    /**
     * Finds the assignment associated with a given route.
     *
     * @param routeId the route identifier
     * @return the assignment if present, otherwise empty
     */
    Optional<Assignment> findByRouteId(RouteId routeId);

    /**
     * Indicates whether an assignment already exists for the given route.
     *
     * @param routeId the route identifier
     * @return true if an assignment already exists for that route
     */
    boolean existsByRouteId(RouteId routeId);

    /**
     * Deletes the assignment attached to the given route, if present.
     *
     * @param routeId the route identifier
     */
    void deleteByRouteId(RouteId routeId);
}
