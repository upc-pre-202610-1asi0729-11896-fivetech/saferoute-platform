package com.acme.saferoute.platform.trip.interfaces.rest.transform;

import com.acme.saferoute.platform.trip.domain.model.entities.Attendance;
import com.acme.saferoute.platform.trip.interfaces.rest.resources.AttendanceResource;

/**
 * Assembler converting an {@link Attendance} domain entity into its {@link AttendanceResource}
 * output form. The owning trip id is supplied by the caller, since an attendance does not hold a
 * back-reference to its trip in the domain model.
 */
public final class AttendanceResourceFromEntityAssembler {

    private AttendanceResourceFromEntityAssembler() {
    }

    /**
     * Builds the output resource from an attendance entity and its owning trip id.
     *
     * @param tripId identifier of the owning trip
     * @param entity the attendance entity
     * @return the corresponding resource
     */
    public static AttendanceResource toResourceFromEntity(Long tripId, Attendance entity) {
        return new AttendanceResource(
                entity.getId(),
                tripId,
                entity.getChildId().childId(),
                entity.getBoardingState().name(),
                entity.getBoardedAt());
    }
}
