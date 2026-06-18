package com.acme.saferoute.platform.trip.application.internal.commandservices;

import com.acme.saferoute.platform.shared.application.result.ApplicationError;
import com.acme.saferoute.platform.shared.application.result.Result;
import com.acme.saferoute.platform.trip.application.commandservices.TripCommandService;
import com.acme.saferoute.platform.trip.domain.model.aggregates.Trip;
import com.acme.saferoute.platform.trip.domain.model.commands.CompleteTripCommand;
import com.acme.saferoute.platform.trip.domain.model.commands.DeleteTripCommand;
import com.acme.saferoute.platform.trip.domain.model.commands.ReportIncidentCommand;
import com.acme.saferoute.platform.trip.domain.model.commands.StartTripCommand;
import com.acme.saferoute.platform.trip.domain.model.commands.UpdateBoardingStatusCommand;
import com.acme.saferoute.platform.trip.domain.model.valueobjects.BoardingState;
import com.acme.saferoute.platform.trip.domain.model.valueobjects.ChildId;
import com.acme.saferoute.platform.trip.domain.repositories.TripRepository;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Trip command service implementation.
 *
 * <p>Orchestrates trip write use cases by loading/creating the {@link Trip} aggregate, delegating
 * behavior and invariants to it, persisting through the {@link TripRepository} port, and translating
 * outcomes into a {@link Result}. Domain exceptions are mapped to structured {@link ApplicationError}
 * failures: invalid input becomes a validation error, while broken state transitions become
 * business-rule violations.</p>
 */
@Service
public class TripCommandServiceImpl implements TripCommandService {

    private final TripRepository tripRepository;

    /**
     * Creates the service with its required repository port.
     *
     * @param tripRepository the trip repository port
     */
    public TripCommandServiceImpl(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    // inherited javadoc
    @Override
    public Result<Trip, ApplicationError> handle(StartTripCommand command) {
        try {
            var trip = new Trip(command);
            return Result.success(tripRepository.save(trip));
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("Trip", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("Trip start", e.getMessage()));
        }
    }

    // inherited javadoc
    @Override
    public Result<Trip, ApplicationError> handle(CompleteTripCommand command) {
        try {
            var tripOptional = tripRepository.findById(command.tripId());
            if (tripOptional.isEmpty()) {
                return Result.failure(ApplicationError.notFound("Trip", String.valueOf(command.tripId())));
            }
            var trip = tripOptional.get();
            trip.complete();
            return Result.success(tripRepository.save(trip));
        } catch (IllegalStateException e) {
            return Result.failure(ApplicationError.businessRuleViolation("Trip completion", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("Trip completion", e.getMessage()));
        }
    }

    // inherited javadoc
    @Override
    public Result<Trip, ApplicationError> handle(UpdateBoardingStatusCommand command) {
        try {
            var tripOptional = tripRepository.findById(command.tripId());
            if (tripOptional.isEmpty()) {
                return Result.failure(ApplicationError.notFound("Trip", String.valueOf(command.tripId())));
            }
            var boardingState = parseBoardingState(command.boardingState());
            var trip = tripOptional.get();
            trip.updateBoardingStatus(new ChildId(command.childId()), boardingState);
            return Result.success(tripRepository.save(trip));
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("Attendance", e.getMessage()));
        } catch (IllegalStateException e) {
            return Result.failure(ApplicationError.businessRuleViolation("Boarding update", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("Boarding update", e.getMessage()));
        }
    }

    // inherited javadoc
    @Override
    public Result<Trip, ApplicationError> handle(ReportIncidentCommand command) {
        try {
            var tripOptional = tripRepository.findById(command.tripId());
            if (tripOptional.isEmpty()) {
                return Result.failure(ApplicationError.notFound("Trip", String.valueOf(command.tripId())));
            }
            var trip = tripOptional.get();
            trip.reportIncident(command.description());
            return Result.success(tripRepository.save(trip));
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("Incident", e.getMessage()));
        } catch (IllegalStateException e) {
            return Result.failure(ApplicationError.businessRuleViolation("Incident report", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("Incident report", e.getMessage()));
        }
    }

    // inherited javadoc
    @Override
    public Result<Long, ApplicationError> handle(DeleteTripCommand command) {
        try {
            if (!tripRepository.existsById(command.tripId())) {
                return Result.failure(ApplicationError.notFound("Trip", String.valueOf(command.tripId())));
            }
            tripRepository.deleteById(command.tripId());
            return Result.success(command.tripId());
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("Trip deletion", e.getMessage()));
        }
    }

    /**
     * Parses the boarding state code into the {@link BoardingState} enum, raising a descriptive
     * validation error for unknown values.
     *
     * @param value the boarding state code
     * @return the parsed boarding state
     * @throws IllegalArgumentException if the value does not match a known boarding state
     */
    private static BoardingState parseBoardingState(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("boardingState must not be null or blank");
        }
        try {
            return BoardingState.valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Unknown boarding state '%s'. Allowed values: PENDING, BOARDED, ABSENT, DROPPED_OFF".formatted(value));
        }
    }
}
