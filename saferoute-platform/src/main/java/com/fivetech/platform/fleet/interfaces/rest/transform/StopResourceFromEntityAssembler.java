package com.fivetech.platform.fleet.interfaces.rest.transform;

import com.fivetech.platform.fleet.domain.model.entities.Stop;
import com.fivetech.platform.fleet.interfaces.rest.resources.StopResource;

/**
 * Assembler converting a {@link Stop} domain entity into its {@link StopResource} output form.
 *
 * <p>The owning route id is supplied by the caller, since a stop is a child entity that does not
 * hold a back-reference to its route in the domain model.</p>
 */
public final class StopResourceFromEntityAssembler {

    private StopResourceFromEntityAssembler() {
    }

    /**
     * Builds the output resource from a stop entity and its owning route id.
     *
     * @param routeId identifier of the owning route
     * @param entity  the stop entity
     * @return the corresponding resource
     */
    public static StopResource toResourceFromEntity(Long routeId, Stop entity) {
        return new StopResource(
                entity.getId(),
                routeId,
                entity.getName(),
                entity.getCoordinates().latitude(),
                entity.getCoordinates().longitude(),
                entity.getStopOrder());
    }
}
