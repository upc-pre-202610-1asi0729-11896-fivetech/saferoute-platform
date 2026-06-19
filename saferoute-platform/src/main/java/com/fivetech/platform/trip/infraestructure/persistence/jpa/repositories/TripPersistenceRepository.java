package com.acme.saferoute.platform.trip.infrastructure.persistence.jpa.repositories;

import com.acme.saferoute.platform.trip.domain.model.valueobjects.OrganizationId;
import com.acme.saferoute.platform.trip.infrastructure.persistence.jpa.entities.TripPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link TripPersistenceEntity}.
 *
 * <p>Low-level persistence access used by the {@code TripRepositoryImpl} adapter.</p>
 */
@Repository
public interface TripPersistenceRepository extends JpaRepository<TripPersistenceEntity, Long> {

    /**
     * Finds all trip persistence entities owned by the given organization.
     *
     * @param organizationId the owning organization identifier value object
     * @return the matching trip entities
     */
    List<TripPersistenceEntity> findAllByOrganizationId(OrganizationId organizationId);
}
