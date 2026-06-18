package com.acme.saferoute.platform.trip.application.commandservices;

import com.acme.saferoute.platform.shared.application.result.ApplicationError;
import com.acme.saferoute.platform.shared.application.result.Result;
import com.acme.saferoute.platform.trip.domain.model.aggregates.Trip;
import com.acme.saferoute.platform.trip.domain.model.commands.CompleteTripCommand;
import com.acme.saferoute.platform.trip.domain.model.commands.DeleteTripCommand;
import com.acme.saferoute.platform.trip.domain.model.commands.ReportIncidentCommand;
import com.acme.saferoute.platform.trip.domain.model.commands.StartTripCommand;
import com.acme.saferoute.platform.trip.domain.model.commands.UpdateBoardingStatusCommand;

/**
 * Application service contract for Trip write operations within the Trip bounded context.
 *
 * <p>Each handler returns a {@link Result} carrying either the affected {@link Trip} aggregate or an
 * {@link ApplicationError}, making validation and business-rule failures explicit.</p>
 */
public interface TripCommandService {

    /**
     * Handles starting a trip (US-10).
     *
     * @param command the {@link StartTripCommand}
     * @return the started trip on success, or an error on validation failure
     */
    Result<Trip, ApplicationError> handle(StartTripCommand command);

    /**
     * Handles completing a trip (US-14).
     *
     * @param command the {@link CompleteTripCommand}
     * @return the completed trip on success, or an error if it does not exist or cannot be completed
     */
    Result<Trip, ApplicationError> handle(CompleteTripCommand command);

    /**
     * Handles updating a child's boarding status (US-11).
     *
     * @param command the {@link UpdateBoardingStatusCommand}
     * @return the updated trip on success, or an error if it does not exist or input is invalid
     */
    Result<Trip, ApplicationError> handle(UpdateBoardingStatusCommand command);

    /**
     * Handles reporting an incident on a trip.
     *
     * @param command the {@link ReportIncidentCommand}
     * @return the updated trip on success, or an error if it does not exist or cannot accept incidents
     */
    Result<Trip, ApplicationError> handle(ReportIncidentCommand command);

    /**
     * Handles deleting a trip.
     *
     * @param command the {@link DeleteTripCommand}
     * @return the id of the deleted trip on success, or an error if it does not exist
     */
    Result<Long, ApplicationError> handle(DeleteTripCommand command);
}
