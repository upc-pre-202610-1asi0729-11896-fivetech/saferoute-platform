package com.acme.saferoute.platform.fleet.interfaces.rest.transform;

import com.acme.saferoute.platform.fleet.domain.model.commands.CreateAssignmentCommand;
import com.acme.saferoute.platform.fleet.interfaces.rest.resources.CreateAssignmentResource;

/**
 * Assembler converting a {@link CreateAssignmentResource} into a {@link CreateAssignmentCommand}.
 */
public final class CreateAssignmentCommandFromResourceAssembler {

    private CreateAssignmentCommandFromResourceAssembler() {
    }

    /**
     * Builds the command from the inbound resource.
     *
     * @param resource the create-assignment resource
     * @return the corresponding command
     */
    public static CreateAssignmentCommand toCommandFromResource(CreateAssignmentResource resource) {
        return new CreateAssignmentCommand(
                resource.routeId(),
                resource.driverId(),
                resource.childIds());
    }
}
