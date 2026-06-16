package com.fivetech.platform.fleet.rest.transform;

import com.fivetech.platform.fleet.domain.model.aggregates.Vehicle;
import com.fivetech.platform.fleet.rest.resources.VehicleResource;

/**
 * Assembler converting a {@link Vehicle} aggregate into its flat {@link VehicleResource} output form.
 */
public final class VehicleResourceFromEntityAssembler {

    private VehicleResourceFromEntityAssembler() {
    }

    /**
     * Builds the output resource from a vehicle aggregate.
     *
     * @param entity the vehicle aggregate
     * @return the corresponding resource
     */
    public static VehicleResource toResourceFromEntity(Vehicle entity) {
        return new VehicleResource(
                entity.getId(),
                entity.getOrganizationId().organizationId(),
                entity.getPlate(),
                entity.getModel(),
                entity.getCapacity(),
                entity.getStatusName());
    }
}
