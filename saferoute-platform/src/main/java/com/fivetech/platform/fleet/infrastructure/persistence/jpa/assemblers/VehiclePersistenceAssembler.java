package com.fivetech.platform.fleet.infrastructure.jpa.assemblers;

import com.fivetech.platform.fleet.domain.model.aggregates.Vehicle;
import com.fivetech.platform.fleet.infrastructure.jpa.entities.VehiclePersistenceEntity;

/**
 * Static assembler that maps between the {@link Vehicle} domain aggregate and its
 * {@link VehiclePersistenceEntity} relational representation.
 */
public final class VehiclePersistenceAssembler {

    private VehiclePersistenceAssembler() {
    }

    /**
     * Rebuilds a {@link Vehicle} domain aggregate from its persistence entity.
     *
     * @param entity the vehicle persistence entity (may be null)
     * @return the corresponding domain vehicle, or null if {@code entity} is null
     */
    public static Vehicle toDomainFromPersistence(VehiclePersistenceEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Vehicle(
                entity.getId(),
                entity.getOrganizationId(),
                entity.getPlate(),
                entity.getModel(),
                entity.getCapacity(),
                entity.getStatus());
    }

    /**
     * Maps a {@link Vehicle} domain aggregate to a new persistence entity ready to be saved.
     *
     * @param vehicle the domain vehicle
     * @return the corresponding persistence entity, or null if {@code vehicle} is null
     */
    public static VehiclePersistenceEntity toPersistenceFromDomain(Vehicle vehicle) {
        if (vehicle == null) {
            return null;
        }
        var entity = new VehiclePersistenceEntity();
        if (vehicle.getId() != null) {
            entity.setId(vehicle.getId());
        }
        entity.setOrganizationId(vehicle.getOrganizationId());
        entity.setPlate(vehicle.getPlate());
        entity.setModel(vehicle.getModel());
        entity.setCapacity(vehicle.getCapacity());
        entity.setStatus(vehicle.getStatus());
        return entity;
    }
}
