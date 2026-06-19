package com.fivetech.platform.fleet.application.commandservices;

import com.fivetech.platform.fleet.domain.model.aggregates.Route;
import com.fivetech.platform.fleet.domain.model.commands.ActivateRouteCommand;
import com.fivetech.platform.fleet.domain.model.commands.AddStopToRouteCommand;
import com.fivetech.platform.fleet.domain.model.commands.CreateRouteCommand;
import com.fivetech.platform.fleet.domain.model.commands.DeleteRouteCommand;
import com.fivetech.platform.fleet.domain.model.commands.ReplaceRouteStopsCommand;
import com.fivetech.platform.fleet.domain.model.commands.UpdateRouteCommand;
import com.fivetech.platform.shared.application.result.ApplicationError;
import com.fivetech.platform.shared.application.result.Result;

/**
 * Application service contract for Route write operations within the Fleet bounded context.
 *
 * <p>Each handler returns a {@link Result} carrying either the affected {@link Route} aggregate
 * or an {@link ApplicationError}, making validation and business-rule failures explicit.</p>
 */
public interface RouteCommandService {

    /**
     * Handles the creation of a new route (US-5).
     *
     * @param command the {@link CreateRouteCommand}
     * @return the created route on success, or an error on validation failure
     */
    Result<Route, ApplicationError> handle(CreateRouteCommand command);

    /**
     * Handles adding a stop to an existing route (US-5 S2).
     *
     * @param command the {@link AddStopToRouteCommand}
     * @return the updated route on success, or an error if the route does not exist or input is invalid
     */
    Result<Route, ApplicationError> handle(AddStopToRouteCommand command);

    /**
     * Handles replacing a route's entire stop sequence (bulk set, used by the edit flow).
     *
     * @param command the {@link ReplaceRouteStopsCommand}
     * @return the updated route on success, or an error if the route does not exist or input is invalid
     */
    Result<Route, ApplicationError> handle(ReplaceRouteStopsCommand command);

    /**
     * Handles activating a route (US-6).
     *
     * @param command the {@link ActivateRouteCommand}
     * @return the activated route on success, or an error if the route does not exist or cannot be activated
     */
    Result<Route, ApplicationError> handle(ActivateRouteCommand command);

    /**
     * Handles updating a route's information and stop sequence.
     *
     * @param command the {@link UpdateRouteCommand}
     * @return the updated route on success, or an error if it does not exist or input is invalid
     */
    Result<Route, ApplicationError> handle(UpdateRouteCommand command);

    /**
     * Handles deleting a route (including its assignment, if any).
     *
     * @param command the {@link DeleteRouteCommand}
     * @return the id of the deleted route on success, or an error if it does not exist
     */
    Result<Long, ApplicationError> handle(DeleteRouteCommand command);
}
