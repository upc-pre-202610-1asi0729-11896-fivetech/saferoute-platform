package com.acme.saferoute.platform.fleet.application.internal.commandservices;

import com.acme.saferoute.platform.fleet.application.commandservices.VehicleCommandService;
import com.acme.saferoute.platform.fleet.domain.model.aggregates.Vehicle;
import com.acme.saferoute.platform.fleet.domain.model.commands.CreateVehicleCommand;
import com.acme.saferoute.platform.fleet.domain.model.commands.UpdateVehicleCommand;
import com.acme.saferoute.platform.fleet.domain.model.valueobjects.OrganizationId;
import com.acme.saferoute.platform.fleet.domain.repositories.VehicleRepository;
import com.acme.saferoute.platform.shared.application.result.ApplicationError;
import com.acme.saferoute.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;

/**
 * Vehicle command service implementation.
 *
 * <p>Enforces the fleet-level uniqueness of a plate within an organization before delegating
 * vehicle creation to the {@link Vehicle} aggregate and persisting through the
 * {@link VehicleRepository} port.</p>
 */
@Service
public class VehicleCommandServiceImpl implements VehicleCommandService {

    private final VehicleRepository vehicleRepository;

    /**
     * Creates the service with its required repository port.
     *
     * @param vehicleRepository the vehicle repository port
     */
    public VehicleCommandServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    // inherited javadoc
    @Override
    public Result<Vehicle, ApplicationError> handle(CreateVehicleCommand command) {
        try {
            var organizationId = new OrganizationId(command.organizationId());
            var normalizedPlate = command.plate() == null ? null : command.plate().trim().toUpperCase();
            if (normalizedPlate != null
                    && vehicleRepository.existsByOrganizationIdAndPlate(organizationId, normalizedPlate)) {
                return Result.failure(ApplicationError.conflict(
                        "Vehicle",
                        "A vehicle with plate '%s' already exists in this organization".formatted(normalizedPlate)));
            }

            var vehicle = new Vehicle(command);
            var savedVehicle = vehicleRepository.save(vehicle);
            return Result.success(savedVehicle);
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("Vehicle", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("Vehicle creation", e.getMessage()));
        }
    }

    // inherited javadoc
    @Override
    public Result<Vehicle, ApplicationError> handle(UpdateVehicleCommand command) {
        try {
            var vehicleOptional = vehicleRepository.findById(command.vehicleId());
            if (vehicleOptional.isEmpty()) {
                return Result.failure(ApplicationError.notFound("Vehicle", String.valueOf(command.vehicleId())));
            }
            var vehicle = vehicleOptional.get();
            vehicle.updateInformation(command);
            var savedVehicle = vehicleRepository.save(vehicle);
            return Result.success(savedVehicle);
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("Vehicle", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("Vehicle update", e.getMessage()));
        }
    }
}
