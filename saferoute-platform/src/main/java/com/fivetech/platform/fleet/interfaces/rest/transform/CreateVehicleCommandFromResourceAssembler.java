package com.fivetech.platform.fleet.interfaces.rest.transform;

import com.fivetech.platform.fleet.domain.model.commands.CreateVehicleCommand;
import com.fivetech.platform.fleet.interfaces.rest.resources.VehicleResource;

/**
 * Assembler converting a flat {@link VehicleResource} into a {@link CreateVehicleCommand}.
 */
public final class CreateVehicleCommandFromResourceAssembler {

    private CreateVehicleCommandFromResourceAssembler() {
    }

    /**
     * Builds the create command from the inbound resource.
     *
     * @param resource the vehicle resource
     * @return the corresponding command
     */
    public static CreateVehicleCommand toCommandFromResource(VehicleResource resource) {
        return new CreateVehicleCommand(
                resource.organizationId(),
                resource.plate(),
                resource.model(),
                resource.capacity(),
                resource.status());
    }
}
