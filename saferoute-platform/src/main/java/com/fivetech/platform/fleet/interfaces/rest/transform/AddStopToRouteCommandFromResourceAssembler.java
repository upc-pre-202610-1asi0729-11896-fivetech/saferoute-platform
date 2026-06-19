package com.acme.saferoute.platform.fleet.interfaces.rest.transform;

import com.acme.saferoute.platform.fleet.domain.model.commands.AddStopToRouteCommand;
import com.acme.saferoute.platform.fleet.interfaces.rest.resources.AddStopResource;

/**
 * Assembler converting an {@link AddStopResource} (plus the route id from the URL path) into an
 * {@link AddStopToRouteCommand}.
 */
public final class AddStopToRouteCommandFromResourceAssembler {

    private AddStopToRouteCommandFromResourceAssembler() {
    }

    /**
     * Builds the command from the route id and the inbound resource.
     *
     * @param routeId  identifier of the route the stop is added to
     * @param resource the add-stop resource
     * @return the corresponding command
     */
    public static AddStopToRouteCommand toCommandFromResource(Long routeId, AddStopResource resource) {
        return new AddStopToRouteCommand(
                routeId,
                resource.name(),
                resource.latitude(),
                resource.longitude(),
                resource.stopOrder());
    }
}
