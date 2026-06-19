package com.acme.saferoute.platform.trip.interfaces.rest.transform;

import com.acme.saferoute.platform.trip.domain.model.commands.StartTripCommand;
import com.acme.saferoute.platform.trip.interfaces.rest.resources.StartTripResource;

/**
 * Assembler converting a {@link StartTripResource} into a {@link StartTripCommand}.
 */
public final class StartTripCommandFromResourceAssembler {

    private StartTripCommandFromResourceAssembler() {
    }

    /**
     * Builds the command from the inbound resource.
     *
     * @param resource the start-trip resource
     * @return the corresponding command
     */
    public static StartTripCommand toCommandFromResource(StartTripResource resource) {
        return new StartTripCommand(
                resource.organizationId(),
                resource.routeId(),
                resource.driverId());
    }
}
