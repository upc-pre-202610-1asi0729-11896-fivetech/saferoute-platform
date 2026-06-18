package com.acme.saferoute.platform.trip.domain.repositories;

import com.acme.saferoute.platform.trip.domain.model.aggregates.Trip;
import com.acme.saferoute.platform.trip.domain.model.valueobjects.OrganizationId;

import java.util.List;
import java.util.Optional;

/**
 * Trip repository port.
 *
 * <p>Domain-facing abstraction for persisting and retrieving {@link Trip} aggregates together with
 * their attendance and incident children. The JPA-backed implementation lives in the infrastructure
 * layer, keeping the domain persistence-agnostic.</p>
 */
public interface TripRepository {

    /**
     * Persists the given trip (insert or update) and returns the saved aggregate.
     *
     * @param trip the trip to save
     * @return the persisted trip, carrying its assigned identity
     */
    Trip save(Trip trip);

    /**
     * Finds a trip by its identity.
     *
     * @param id the trip identity
     * @return the trip if present, otherwise empty
     */
    Optional<Trip> findById(Long id);

    /**
     * Finds all trips owned by the given organization.
     *
     * @param organizationId the owning organization identifier
     * @return the matching trips (possibly empty)
     */
    List<Trip> findAllByOrganizationId(OrganizationId organizationId);

    /**
     * Indicates whether a trip with the given identity exists.
     *
     * @param id the trip identity
     * @return true if the trip exists
     */
    boolean existsById(Long id);

    /**
     * Deletes the trip with the given identity, if present.
     *
     * @param id the trip identity
     */
    void deleteById(Long id);
}
