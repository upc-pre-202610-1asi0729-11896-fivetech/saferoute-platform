package com.fivetech.platform.fleet.domain.repositories;

import com.fivetech.platform.fleet.domain.model.aggregates.Vehicle;
import com.acme.saferoute.platform.fleet.domain.model.valueobjects.OrganizationId;

import java.util.List;
import java.util.Optional;

/**
 * Vehicle repository port.
 *
 * <p>Domain-facing abstraction for persisting and retrieving {@link Vehicle} aggregates.
 * Includes a uniqueness check on plate to enforce the fleet-level invariant.</p>
 */
public interface VehicleRepository {

    /**
     * Persists the given vehicle (insert or update) and returns the saved aggregate.
     *
     * @param vehicle the vehicle to save
     * @return the persisted vehicle, carrying its assigned identity
     */
    Vehicle save(Vehicle vehicle);

    /**
     * Finds a vehicle by its identity.
     *
     * @param id the vehicle identity
     * @return the vehicle if present, otherwise empty
     */
    Optional<Vehicle> findById(Long id);

    /**
     * Finds all vehicles owned by the given organization.
     *
     * @param organizationId the owning organization identifier
     * @return the matching vehicles (possibly empty)
     */
    List<Vehicle> findAllByOrganizationId(OrganizationId organizationId);

    /**
     * Indicates whether a vehicle with the given plate already exists within an organization.
     *
     * @param organizationId the owning organization identifier
     * @param plate          the license plate to check
     * @return true if a vehicle with that plate already exists in the organization
     */
    boolean existsByOrganizationIdAndPlate(OrganizationId organizationId, String plate);

    /**
     * Indicates whether a vehicle with the given identity exists.
     *
     * @param id the vehicle identity
     * @return true if the vehicle exists
     */
    boolean existsById(Long id);

    /**
     * Deletes the vehicle with the given identity, if present.
     *
     * @param id the vehicle identity
     */
    void deleteById(Long id);
}
