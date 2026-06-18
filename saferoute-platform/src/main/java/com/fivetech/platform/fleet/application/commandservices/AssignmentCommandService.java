package com.acme.saferoute.platform.fleet.application.commandservices;

import com.acme.saferoute.platform.fleet.domain.model.aggregates.Assignment;
import com.acme.saferoute.platform.fleet.domain.model.commands.CreateAssignmentCommand;
import com.acme.saferoute.platform.fleet.domain.model.commands.UpdateAssignmentCommand;
import com.acme.saferoute.platform.shared.application.result.ApplicationError;
import com.acme.saferoute.platform.shared.application.result.Result;

/**
 * Application service contract for Assignment write operations within the Fleet bounded context.
 */
public interface AssignmentCommandService {

    /**
     * Handles assigning a driver and children to a route (US-6).
     *
     * @param command the {@link CreateAssignmentCommand}
     * @return the created assignment on success, or an error on validation/conflict failure
     */
    Result<Assignment, ApplicationError> handle(CreateAssignmentCommand command);

    /**
     * Handles replacing the staffing of the assignment attached to a route (US-6 reassignment).
     *
     * @param command the {@link UpdateAssignmentCommand}
     * @return the updated assignment on success, or an error if no assignment exists for the route
     */
    Result<Assignment, ApplicationError> handle(UpdateAssignmentCommand command);
}
