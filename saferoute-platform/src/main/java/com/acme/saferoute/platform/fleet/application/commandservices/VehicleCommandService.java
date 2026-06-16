package com.acme.saferoute.platform.fleet.application.commandservices;

import com.acme.saferoute.platform.fleet.domain.model.aggregates.Vehicle;
import com.acme.saferoute.platform.fleet.domain.model.commands.CreateVehicleCommand;
import com.acme.saferoute.platform.fleet.domain.model.commands.DeleteVehicleCommand;
import com.acme.saferoute.platform.fleet.domain.model.commands.UpdateVehicleCommand;
import com.acme.saferoute.platform.shared.application.result.ApplicationError;
import com.acme.saferoute.platform.shared.application.result.Result;

/**
 * Application service contract for Vehicle write operations within the Fleet bounded context.
 */
public interface VehicleCommandService {

    /**
     * Handles the registration of a new vehicle.
     *
     * @param command the {@link CreateVehicleCommand}
     * @return the created vehicle on success, or an error on validation/conflict failure
     */
    Result<Vehicle, ApplicationError> handle(CreateVehicleCommand command);

    /**
     * Handles updating an existing vehicle.
     *
     * @param command the {@link UpdateVehicleCommand}
     * @return the updated vehicle on success, or an error if it does not exist or input is invalid
     */
    Result<Vehicle, ApplicationError> handle(UpdateVehicleCommand command);

    /**
     * Handles deleting a vehicle.
     *
     * @param command the {@link DeleteVehicleCommand}
     * @return the id of the deleted vehicle on success, or an error if it does not exist
     */
    Result<Long, ApplicationError> handle(DeleteVehicleCommand command);
}
