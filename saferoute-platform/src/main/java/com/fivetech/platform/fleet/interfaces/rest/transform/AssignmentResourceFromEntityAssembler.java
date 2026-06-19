package com.acme.saferoute.platform.fleet.interfaces.rest.transform;

import com.acme.saferoute.platform.fleet.domain.model.aggregates.Assignment;
import com.acme.saferoute.platform.fleet.domain.model.valueobjects.ChildId;
import com.acme.saferoute.platform.fleet.interfaces.rest.resources.AssignmentResource;

/**
 * Assembler converting an {@link Assignment} aggregate into its {@link AssignmentResource}
 * output form, unwrapping the child identifier value objects into plain values.
 */
public final class AssignmentResourceFromEntityAssembler {

    private AssignmentResourceFromEntityAssembler() {
    }

    /**
     * Builds the output resource from an assignment aggregate.
     *
     * @param entity the assignment aggregate
     * @return the corresponding resource
     */
    public static AssignmentResource toResourceFromEntity(Assignment entity) {
        return new AssignmentResource(
                entity.getId(),
                entity.getRouteId().routeId(),
                entity.getDriverId().driverId(),
                entity.getChildIds().stream().map(ChildId::childId).toList());
    }
}
