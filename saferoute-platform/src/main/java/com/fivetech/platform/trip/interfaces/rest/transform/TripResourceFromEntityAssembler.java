package com.acme.saferoute.platform.trip.interfaces.rest.transform;

import com.acme.saferoute.platform.trip.domain.model.aggregates.Trip;
import com.acme.saferoute.platform.trip.interfaces.rest.resources.TripResource;

/**
 * Assembler converting a {@link Trip} aggregate into its {@link TripResource} output form,
 * unwrapping value objects into the primitive shape expected by the frontend.
 */
public final class TripResourceFromEntityAssembler {

    private TripResourceFromEntityAssembler() {
    }

    /**
     * Builds the output resource from a trip aggregate.
     *
     * @param entity the trip aggregate
     * @return the corresponding resource
     */
    public static TripResource toResourceFromEntity(Trip entity) {
        return new TripResource(
                entity.getId(),
                entity.getOrganizationId().organizationId(),
                entity.getRouteId().routeId(),
                entity.getDriverId().driverId(),
                entity.getStateName(),
                entity.getStartTime(),
                entity.getEndTime());
    }
}
