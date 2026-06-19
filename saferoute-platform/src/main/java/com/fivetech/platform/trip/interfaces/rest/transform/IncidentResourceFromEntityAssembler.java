package com.acme.saferoute.platform.trip.interfaces.rest.transform;

import com.acme.saferoute.platform.trip.domain.model.entities.Incident;
import com.acme.saferoute.platform.trip.interfaces.rest.resources.IncidentResource;

/**
 * Assembler converting an {@link Incident} domain entity into its {@link IncidentResource} output
 * form. The owning trip id is supplied by the caller, since an incident does not hold a
 * back-reference to its trip in the domain model.
 */
public final class IncidentResourceFromEntityAssembler {

    private IncidentResourceFromEntityAssembler() {
    }

    /**
     * Builds the output resource from an incident entity and its owning trip id.
     *
     * @param tripId identifier of the owning trip
     * @param entity the incident entity
     * @return the corresponding resource
     */
    public static IncidentResource toResourceFromEntity(Long tripId, Incident entity) {
        return new IncidentResource(
                entity.getId(),
                tripId,
                entity.getDescription(),
                entity.getReportedAt());
    }
}
