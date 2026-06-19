package com.acme.saferoute.platform.fleet.infrastructure.persistence.jpa.repositories;

import com.acme.saferoute.platform.fleet.domain.model.valueobjects.RouteId;
import com.acme.saferoute.platform.fleet.infrastructure.persistence.jpa.entities.AssignmentPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for {@link AssignmentPersistenceEntity}.
 */
@Repository
public interface AssignmentPersistenceRepository extends JpaRepository<AssignmentPersistenceEntity, Long> {

    /**
     * Finds the assignment persistence entity for the given route.
     *
     * @param routeId the route identifier value object
     * @return the assignment entity if present, otherwise empty
     */
    Optional<AssignmentPersistenceEntity> findByRouteId(RouteId routeId);

    /**
     * Indicates whether an assignment already exists for the given route.
     *
     * @param routeId the route identifier value object
     * @return true if an assignment already exists for that route
     */
    boolean existsByRouteId(RouteId routeId);

    /**
     * Deletes the assignment persistence entity attached to the given route, if present.
     *
     * @param routeId the route identifier value object
     */
    void deleteByRouteId(RouteId routeId);
}
