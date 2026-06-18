package com.fivetech.platform.fleet.domain.repositories;

import com.fivetech.platform.fleet.domain.model.aggregates.Route;
import com.fivetech.platform.fleet.domain.model.valueobjects.OrganizationId;

import java.util.List;
import java.util.Optional;

/**
 * Route repository port.
 *
 * <p>Domain-facing abstraction for persisting and retrieving {@link Route} aggregates. The
 * domain depends only on this interface; the JPA-backed implementation lives in the
 * infrastructure layer, keeping the domain persistence-agnostic.</p>
 */
public interface RouteRepository {

    /**
     * Persists the given route (insert or update) and returns the saved aggregate.
     *
     * @param route the route to save
     * @return the persisted route, carrying its assigned identity
     */
    Route save(Route route);

    /**
     * Finds a route by its identity.
     *
     * @param id the route identity
     * @return the route if present, otherwise empty
     */
    Optional<Route> findById(Long id);

    /**
     * Finds all routes owned by the given organization.
     *
     * @param organizationId the owning organization identifier
     * @return the matching routes (possibly empty)
     */
    List<Route> findAllByOrganizationId(OrganizationId organizationId);

    /**
     * Indicates whether a route with the given identity exists.
     *
     * @param id the route identity
     * @return true if the route exists
     */
    boolean existsById(Long id);

    /**
     * Deletes the route with the given identity, if present.
     *
     * @param id the route identity
     */
    void deleteById(Long id);
}
