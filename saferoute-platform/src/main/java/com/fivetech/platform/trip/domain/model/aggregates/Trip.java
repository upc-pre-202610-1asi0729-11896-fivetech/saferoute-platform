package com.acme.saferoute.platform.trip.domain.model.aggregates;

import com.acme.saferoute.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.acme.saferoute.platform.trip.domain.model.commands.StartTripCommand;
import com.acme.saferoute.platform.trip.domain.model.entities.Attendance;
import com.acme.saferoute.platform.trip.domain.model.entities.Incident;
import com.acme.saferoute.platform.trip.domain.model.events.BoardingOpenedEvent;
import com.acme.saferoute.platform.trip.domain.model.events.BoardingStatusUpdatedEvent;
import com.acme.saferoute.platform.trip.domain.model.events.IncidentReportedEvent;
import com.acme.saferoute.platform.trip.domain.model.events.StudentBoardedEvent;
import com.acme.saferoute.platform.trip.domain.model.events.StudentDroppedOffEvent;
import com.acme.saferoute.platform.trip.domain.model.events.TripCompletedEvent;
import com.acme.saferoute.platform.trip.domain.model.events.TripStartedEvent;
import com.acme.saferoute.platform.trip.domain.model.valueobjects.BoardingState;
import com.acme.saferoute.platform.trip.domain.model.valueobjects.ChildId;
import com.acme.saferoute.platform.trip.domain.model.valueobjects.DriverId;
import com.acme.saferoute.platform.trip.domain.model.valueobjects.OrganizationId;
import com.acme.saferoute.platform.trip.domain.model.valueobjects.RouteId;
import com.acme.saferoute.platform.trip.domain.model.valueobjects.TripState;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Trip aggregate root — the heart of the Trip bounded context.
 *
 * <p>Represents the execution and monitoring of a single run of a route by a driver (US-10, US-11,
 * US-14, US-20). The trip is the consistency boundary for its {@link Attendance} and {@link Incident}
 * child entities and for its lifecycle ({@link TripState}). All state transitions and invariants are
 * enforced here; persistence concerns live in {@code TripPersistenceEntity}.</p>
 */
@Getter
public class Trip extends AbstractDomainAggregateRoot<Trip> {

    /**
     * Persistence identity. Assigned by the infrastructure layer; null until persisted.
     */
    private Long id;

    /**
     * Identifier of the organization the trip belongs to.
     */
    private OrganizationId organizationId;

    /**
     * Identifier of the route being executed.
     */
    private RouteId routeId;

    /**
     * Identifier of the driver operating the trip.
     */
    private DriverId driverId;

    /**
     * Current lifecycle state of the trip.
     */
    private TripState tripState;

    /**
     * Timestamp at which the trip started.
     */
    private Instant startTime;

    /**
     * Timestamp at which the trip ended, or null while in progress.
     */
    private Instant endTime;

    /**
     * Boarding records for children tracked on this trip.
     */
    private final List<Attendance> attendances = new ArrayList<>();

    /**
     * Incidents reported during this trip.
     */
    private final List<Incident> incidents = new ArrayList<>();

    /**
     * Default constructor required for reconstruction from persistence.
     */
    public Trip() {
    }

    /**
     * Starts a new trip from a start command. The trip begins IN_PROGRESS at the current instant.
     *
     * @param command the {@link StartTripCommand}
     */
    public Trip(StartTripCommand command) {
        this.organizationId = new OrganizationId(command.organizationId());
        this.routeId = new RouteId(command.routeId());
        this.driverId = new DriverId(command.driverId());
        this.tripState = TripState.IN_PROGRESS;
        this.startTime = Instant.now();
    }

    /**
     * Full reconstruction constructor used by the persistence assembler.
     *
     * @param id             persistence identity
     * @param organizationId owning organization identifier
     * @param routeId        executed route identifier
     * @param driverId       operating driver identifier
     * @param tripState      lifecycle state
     * @param startTime      start timestamp
     * @param endTime        end timestamp, or null
     * @param attendances    boarding records
     * @param incidents      reported incidents
     */
    public Trip(Long id,
                OrganizationId organizationId,
                RouteId routeId,
                DriverId driverId,
                TripState tripState,
                Instant startTime,
                Instant endTime,
                List<Attendance> attendances,
                List<Incident> incidents) {
        this.id = id;
        this.organizationId = organizationId;
        this.routeId = routeId;
        this.driverId = driverId;
        this.tripState = tripState;
        this.startTime = startTime;
        this.endTime = endTime;
        if (attendances != null) {
            this.attendances.addAll(attendances);
        }
        if (incidents != null) {
            this.incidents.addAll(incidents);
        }
    }

    /**
     * Signals that this trip has just been started and persisted, registering the Event Storming
     * facts established at start: {@link TripStartedEvent} and {@link BoardingOpenedEvent} (boarding
     * opens as soon as the trip starts).
     */
    public void onStarted() {
        registerDomainEvent(new TripStartedEvent(
                this.id,
                this.routeId.routeId(),
                this.driverId.driverId(),
                this.startTime));
        registerDomainEvent(new BoardingOpenedEvent(this.id));
    }

    /**
     * Updates the boarding status of a child (US-11). Creates the attendance record on first use.
     *
     * <p>Business rule: boarding can only be updated while the trip is IN_PROGRESS.</p>
     *
     * @param childId       identifier of the child
     * @param boardingState the new boarding state
     * @throws IllegalStateException if the trip is not in progress
     */
    public void updateBoardingStatus(ChildId childId, BoardingState boardingState) {
        if (this.tripState != TripState.IN_PROGRESS) {
            throw new IllegalStateException("Boarding can only be updated while the trip is in progress");
        }
        var attendance = attendances.stream()
                .filter(a -> a.getChildId().equals(childId))
                .findFirst()
                .orElseGet(() -> {
                    var created = new Attendance(childId);
                    attendances.add(created);
                    return created;
                });
        attendance.changeState(boardingState);
        // Emit the specific Event Storming fact for boarding/drop-off; fall back to the generic
        // boarding-status event for other transitions (e.g. ABSENT, PENDING).
        switch (boardingState) {
            case BOARDED -> registerDomainEvent(new StudentBoardedEvent(this.id, childId.childId()));
            case DROPPED_OFF -> registerDomainEvent(new StudentDroppedOffEvent(this.id, childId.childId()));
            default -> registerDomainEvent(new BoardingStatusUpdatedEvent(
                    this.id, childId.childId(), boardingState.name()));
        }
    }

    /**
     * Reports an incident during the trip.
     *
     * <p>Business rule: incidents can only be reported while the trip has not been completed.</p>
     *
     * @param description the incident description
     * @return the created {@link Incident}
     * @throws IllegalStateException if the trip is already completed
     */
    public Incident reportIncident(String description) {
        if (this.tripState == TripState.COMPLETED) {
            throw new IllegalStateException("Incidents cannot be reported on a completed trip");
        }
        var incident = new Incident(description);
        this.incidents.add(incident);
        registerDomainEvent(new IncidentReportedEvent(this.id, description));
        return incident;
    }

    /**
     * Completes the trip (US-14).
     *
     * <p>Business rules: a trip can only be completed once, and it cannot be completed while any
     * child is still on board (US-14 S2: "Hay alumnos a bordo"). On success, the end time is stamped
     * and a {@link TripCompletedEvent} is registered.</p>
     *
     * @throws IllegalStateException if the trip is already completed or children remain on board
     */
    public void complete() {
        if (this.tripState == TripState.COMPLETED) {
            throw new IllegalStateException("Trip is already completed");
        }
        if (hasChildrenOnBoard()) {
            throw new IllegalStateException("Trip cannot be completed while children are still on board");
        }
        this.tripState = TripState.COMPLETED;
        this.endTime = Instant.now();
        registerDomainEvent(new TripCompletedEvent(this.id, this.routeId.routeId(), this.endTime));
    }

    /**
     * Indicates whether any tracked child is currently on board.
     *
     * @return true if at least one attendance is in the BOARDED state
     */
    public boolean hasChildrenOnBoard() {
        return attendances.stream().anyMatch(Attendance::isOnBoard);
    }

    /**
     * Returns the trip state name, matching the frontend {@code tripState} contract.
     *
     * @return the trip state name (e.g. "IN_PROGRESS")
     */
    public String getStateName() {
        return this.tripState.name();
    }

    /**
     * Assigns the persistence identity. Used by assemblers during reconstruction.
     *
     * @param id the identity to assign
     */
    public void setId(Long id) {
        this.id = id;
    }
}
