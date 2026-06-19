package com.fivetech.platform.fleet.infrastructure.persistence.jpa.repositories;

import com.fivetech.platform.fleet.domain.model.valueobjects.OrganizationId;
import com.fivetech.platform.fleet.infrastructure.persistence.jpa.entities.RoutePersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link RoutePersistenceEntity}.
 *
 * <p>Low-level persistence access used by the {@code RouteRepositoryImpl} adapter. Queries are
 * derived from method names; the {@code OrganizationId} value object is bound through its
 * registered attribute converter.</p>
 */
@Repository
public interface RoutePersistenceRepository extends JpaRepository<RoutePersistenceEntity, Long> {

    /**
     * Finds all route persistence entities owned by the given organization.
     *
     * @param organizationId the owning organization identifier value object
     * @return the matching route entities
     */
    List<RoutePersistenceEntity> findAllByOrganizationId(OrganizationId organizationId);
}
