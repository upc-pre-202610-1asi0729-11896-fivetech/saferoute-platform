package com.acme.saferoute.platform.trip.interfaces.rest.transform;

import com.acme.saferoute.platform.trip.domain.model.commands.UpdateBoardingStatusCommand;
import com.acme.saferoute.platform.trip.interfaces.rest.resources.UpdateBoardingResource;

/**
 * Assembler converting an {@link UpdateBoardingResource} (plus the trip id from the URL path) into
 * an {@link UpdateBoardingStatusCommand}.
 */
public final class UpdateBoardingStatusCommandFromResourceAssembler {

    private UpdateBoardingStatusCommandFromResourceAssembler() {
    }

    /**
     * Builds the command from the trip id and the inbound resource.
     *
     * @param tripId   identifier of the trip
     * @param resource the update-boarding resource
     * @return the corresponding command
     */
    public static UpdateBoardingStatusCommand toCommandFromResource(Long tripId, UpdateBoardingResource resource) {
        return new UpdateBoardingStatusCommand(
                tripId,
                resource.childId(),
                resource.boardingState());
    }
}
