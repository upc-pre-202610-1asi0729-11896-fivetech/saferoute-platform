package com.fivetech.platform.fleet.infrastructure.jpa.adapters;

import com.fivetech.platform.fleet.domain.model.aggregates.Vehicle;
import com.acme.saferoute.platform.fleet.domain.model.valueobjects.OrganizationId;
import com.fivetech.platform.fleet.domain.repositories.VehicleRepository;
import com.fivetech.platform.fleet.infrastructure.jpa.assemblers.VehiclePersistenceAssembler;
import com.fivetech.platform.fleet.infrastructure.jpa.repositories.VehiclePersistenceRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository adapter bridging the {@link VehicleRepository} domain port with Spring Data JPA.
 *
 * <p>Publishes the {@code VehicleCreatedEvent} after a new vehicle is persisted and its JPA
 * identity becomes available.</p>
 */
@Repository
public class VehicleRepositoryImpl implements VehicleRepository {

    private final VehiclePersistenceRepository vehiclePersistenceRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Creates the adapter with its Spring Data repository and the event publisher.
     *
     * @param vehiclePersistenceRepository the Spring Data JPA repository
     * @param eventPublisher               the application event publisher
     */
    public VehicleRepositoryImpl(VehiclePersistenceRepository vehiclePersistenceRepository,
                                 ApplicationEventPublisher eventPublisher) {
        this.vehiclePersistenceRepository = vehiclePersistenceRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        boolean isNew = vehicle.getId() == null;

        var pendingEvents = new ArrayList<>(vehicle.domainEvents());
        vehicle.clearDomainEvents();

        var savedEntity = vehiclePersistenceRepository.save(VehiclePersistenceAssembler.toPersistenceFromDomain(vehicle));
        var savedVehicle = VehiclePersistenceAssembler.toDomainFromPersistence(savedEntity);

        if (isNew) {
            savedVehicle.onCreated();
            pendingEvents.addAll(savedVehicle.domainEvents());
            savedVehicle.clearDomainEvents();
        }

        pendingEvents.forEach(eventPublisher::publishEvent);
        return savedVehicle;
    }

    @Override
    public Optional<Vehicle> findById(Long id) {
        return vehiclePersistenceRepository.findById(id).map(VehiclePersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<Vehicle> findAllByOrganizationId(OrganizationId organizationId) {
        return vehiclePersistenceRepository.findAllByOrganizationId(organizationId).stream()
                .map(VehiclePersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public boolean existsByOrganizationIdAndPlate(OrganizationId organizationId, String plate) {
        return vehiclePersistenceRepository.existsByOrganizationIdAndPlate(organizationId, plate);
    }

    @Override
    public boolean existsById(Long id) {
        return vehiclePersistenceRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        vehiclePersistenceRepository.deleteById(id);
    }
}
