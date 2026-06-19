package com.fivetech.platform.fleet.interfaces.rest.transform;

import com.fivetech.platform.fleet.domain.model.commands.CreateRouteCommand;
import com.fivetech.platform.fleet.interfaces.rest.resources.RouteResource;

import java.util.List;

/**
 * Assembler converting the flat front-end {@link RouteResource} into a {@link CreateRouteCommand}.
 *
 * <p>The front-end does not send the operating {@code serviceDays}, so a sensible Monday–Friday
 * default is applied; {@code scheduledStartTime} maps to the route departure time. Stops are not part
 * of route creation — they are managed through the dedicated stop endpoints.</p>
 */
public final class CreateRouteCommandFromResourceAssembler {

    /** Default operating days applied when the front-end omits them. */
    private static final List<String> DEFAULT_SERVICE_DAYS = List.of("MON", "TUE", "WED", "THU", "FRI");

    private CreateRouteCommandFromResourceAssembler() {
    }

    /**
     * Builds the create command from the flat route resource.
     *
     * @param resource the inbound flat route resource
     * @return the corresponding create command
     */
    public static CreateRouteCommand toCommandFromResource(RouteResource resource) {
        return new CreateRouteCommand(
                resource.name(),
                resource.organizationId(),
                resource.scheduledStartTime(),
                DEFAULT_SERVICE_DAYS,
                resource.vehicleId(),
                resource.type());
    }
}
