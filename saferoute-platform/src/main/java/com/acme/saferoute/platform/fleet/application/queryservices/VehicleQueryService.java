package com.acme.saferoute.platform.fleet.application.queryservices;

import com.acme.saferoute.platform.fleet.domain.model.aggregates.Vehicle;
import com.acme.saferoute.platform.fleet.domain.model.queries.GetAllVehiclesByOrganizationIdQuery;
import com.acme.saferoute.platform.fleet.domain.model.queries.GetVehicleByIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Application service contract for Vehicle read operations within the Fleet bounded context.
 */
public interface VehicleQueryService {

    /**
     * Resolves a vehicle by its identity.
     *
     * @param query the {@link GetVehicleByIdQuery}
     * @return the vehicle if found, otherwise empty
     */
    Optional<Vehicle> handle(GetVehicleByIdQuery query);

    /**
     * Resolves all vehicles for an organization.
     *
     * @param query the {@link GetAllVehiclesByOrganizationIdQuery}
     * @return the matching vehicles (possibly empty)
     */
    List<Vehicle> handle(GetAllVehiclesByOrganizationIdQuery query);
}
