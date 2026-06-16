package com.acme.saferoute.platform.fleet.application.internal.queryservices;

import com.acme.saferoute.platform.fleet.application.queryservices.VehicleQueryService;
import com.acme.saferoute.platform.fleet.domain.model.aggregates.Vehicle;
import com.acme.saferoute.platform.fleet.domain.model.queries.GetAllVehiclesByOrganizationIdQuery;
import com.acme.saferoute.platform.fleet.domain.model.queries.GetVehicleByIdQuery;
import com.acme.saferoute.platform.fleet.domain.model.valueobjects.OrganizationId;
import com.acme.saferoute.platform.fleet.domain.repositories.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Vehicle query service implementation.
 *
 * <p>Resolves Fleet vehicle read models through the {@link VehicleRepository} port.</p>
 */
@Service
public class VehicleQueryServiceImpl implements VehicleQueryService {

    private final VehicleRepository vehicleRepository;

    /**
     * Creates the service with its required repository port.
     *
     * @param vehicleRepository the vehicle repository port
     */
    public VehicleQueryServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    // inherited javadoc
    @Override
    public Optional<Vehicle> handle(GetVehicleByIdQuery query) {
        return vehicleRepository.findById(query.vehicleId());
    }

    // inherited javadoc
    @Override
    public List<Vehicle> handle(GetAllVehiclesByOrganizationIdQuery query) {
        return vehicleRepository.findAllByOrganizationId(new OrganizationId(query.organizationId()));
    }
}
