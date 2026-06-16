package com.acme.saferoute.platform.fleet.interfaces.rest.transform;

import com.acme.saferoute.platform.fleet.domain.model.commands.UpdateVehicleCommand;
import com.acme.saferoute.platform.fleet.interfaces.rest.resources.VehicleResource;

/**
 * Assembler converting a flat {@link VehicleResource} (plus the vehicle id from the URL path) into
 * an {@link UpdateVehicleCommand}.
 */
public final class UpdateVehicleCommandFromResourceAssembler {

    private UpdateVehicleCommandFromResourceAssembler() {
    }

    /**
     * Builds the update command from the vehicle id and the inbound resource.
     *
     * @param vehicleId identifier of the vehicle to update
     * @param resource  the vehicle resource
     * @return the corresponding command
     */
    public static UpdateVehicleCommand toCommandFromResource(Long vehicleId, VehicleResource resource) {
        return new UpdateVehicleCommand(
                vehicleId,
                resource.plate(),
                resource.model(),
                resource.capacity(),
                resource.status());
    }
}
