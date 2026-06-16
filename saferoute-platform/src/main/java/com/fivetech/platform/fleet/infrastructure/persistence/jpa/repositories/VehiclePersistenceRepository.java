package com.fivetech.platform.fleet.infrastructure.persistence.jpa.repositories;

import com.acme.saferoute.platform.fleet.domain.model.valueobjects.OrganizationId;
import com.fivetech.platform.fleet.infrastructure.persistence.jpa.entities.VehiclePersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link VehiclePersistenceEntity}.
 */
@Repository
public interface VehiclePersistenceRepository extends JpaRepository<VehiclePersistenceEntity, Long> {

    /**
     * Finds all vehicle persistence entities owned by the given organization.
     *
     * @param organizationId the owning organization identifier value object
     * @return the matching vehicle entities
     */
    List<VehiclePersistenceEntity> findAllByOrganizationId(OrganizationId organizationId);

    /**
     * Indicates whether a vehicle with the given plate already exists in the organization.
     *
     * @param organizationId the owning organization identifier value object
     * @param plate          the license plate
     * @return true if such a vehicle already exists
     */
    boolean existsByOrganizationIdAndPlate(OrganizationId organizationId, String plate);
}
