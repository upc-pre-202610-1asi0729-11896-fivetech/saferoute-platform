package com.acme.saferoute.platform.fleet.domain.model.aggregates;

import com.acme.saferoute.platform.fleet.domain.model.commands.CreateRouteCommand;
import com.acme.saferoute.platform.fleet.domain.model.commands.RouteStopData;
import com.acme.saferoute.platform.fleet.domain.model.commands.UpdateRouteCommand;
import com.acme.saferoute.platform.fleet.domain.model.entities.Stop;
import com.acme.saferoute.platform.fleet.domain.model.events.DepartureTimeSetEvent;
import com.acme.saferoute.platform.fleet.domain.model.events.RouteActivationFinalizedEvent;
import com.acme.saferoute.platform.fleet.domain.model.events.RouteDefinedEvent;
import com.acme.saferoute.platform.fleet.domain.model.events.ServiceDaysDefinedEvent;
import com.acme.saferoute.platform.fleet.domain.model.events.StopCountUpdatedEvent;
import com.acme.saferoute.platform.fleet.domain.model.events.VehicleAssignedToRouteEvent;
import com.acme.saferoute.platform.fleet.domain.model.valueobjects.Coordinates;
import com.acme.saferoute.platform.fleet.domain.model.valueobjects.DepartureTime;
import com.acme.saferoute.platform.fleet.domain.model.valueobjects.OrganizationId;
import com.acme.saferoute.platform.fleet.domain.model.valueobjects.RouteState;
import com.acme.saferoute.platform.fleet.domain.model.valueobjects.RouteType;
import com.acme.saferoute.platform.fleet.domain.model.valueobjects.ServiceDays;
import com.acme.saferoute.platform.fleet.domain.model.valueobjects.VehicleId;
import com.acme.saferoute.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Route aggregate root.
 *
 * <p>Represents a school-transport route within the Fleet bounded context. A route is owned by
 * an organization, departs at a fixed daily time on a set of service days, may have an assigned
 * vehicle, and aggregates an ordered list of {@link Stop} child entities.</p>
 *
 * <p>The route is the consistency boundary for its stops and its lifecycle state: it governs
 * stop ordering and the {@link RouteState} transitions (DRAFT -> ACTIVE -> INACTIVE). No JPA or
 * persistence annotation appears here; mapping lives in {@code RoutePersistenceEntity}.</p>
 */
@Getter
public class Route extends AbstractDomainAggregateRoot<Route> {

    /**
     * Persistence identity. Assigned by the infrastructure layer; null until persisted.
     */
    private Long id;

    /**
     * Human-readable route name.
     */
    private String name;

    /**
     * Identifier of the organization that owns the route.
     */
    private OrganizationId organizationId;

    /**
     * Current lifecycle state of the route.
     */
    private RouteState routeState;

    /**
     * Direction of the route (outbound to school / inbound back home).
     */
    private RouteType routeType;

    /**
     * Daily departure time of the route.
     */
    private DepartureTime departureTime;

    /**
     * Weekdays on which the route operates.
     */
    private ServiceDays serviceDays;

    /**
     * Identifier of the vehicle assigned to the route; may be null while unassigned.
     */
    private VehicleId vehicleId;

    /**
     * Ordered list of stops along the route.
     */
    private final List<Stop> stops = new ArrayList<>();

    /**
     * Default constructor required for reconstruction from persistence.
     */
    public Route() {
    }

    /**
     * Full reconstruction constructor used by the persistence assembler to rebuild a route
     * from its stored representation without re-running creation side effects.
     *
     * @param id             persistence identity
     * @param name           route name
     * @param organizationId owning organization identifier
     * @param routeState     current lifecycle state
     * @param routeType      route direction; null defaults to OUTBOUND
     * @param departureTime  daily departure time
     * @param serviceDays    operating weekdays
     * @param vehicleId      assigned vehicle identifier, or null
     * @param stops          ordered list of stops
     */
    public Route(Long id,
                 String name,
                 OrganizationId organizationId,
                 RouteState routeState,
                 RouteType routeType,
                 DepartureTime departureTime,
                 ServiceDays serviceDays,
                 VehicleId vehicleId,
                 List<Stop> stops) {
        this.id = id;
        this.name = validateName(name);
        this.organizationId = Objects.requireNonNull(organizationId, "organizationId must not be null");
        this.routeState = Objects.requireNonNull(routeState, "routeState must not be null");
        this.routeType = routeType == null ? RouteType.OUTBOUND : routeType;
        this.departureTime = Objects.requireNonNull(departureTime, "departureTime must not be null");
        this.serviceDays = Objects.requireNonNull(serviceDays, "serviceDays must not be null");
        this.vehicleId = vehicleId;
        if (stops != null) {
            this.stops.addAll(stops);
        }
    }

    /**
     * Creates a new route in DRAFT state from a creation command.
     *
     * @param command the {@link CreateRouteCommand} carrying the route data
     */
    public Route(CreateRouteCommand command) {
        this.name = validateName(command.name());
        this.organizationId = new OrganizationId(command.organizationId());
        this.departureTime = DepartureTime.of(command.departureTime());
        this.serviceDays = new ServiceDays(command.serviceDays());
        this.vehicleId = command.vehicleId() == null ? null : new VehicleId(command.vehicleId());
        this.routeType = RouteType.fromNullable(command.routeType());
        this.routeState = RouteState.DRAFT;
        // Stops are not created here: they are added separately via the dedicated stop endpoints
        // (addStop / replaceStops), keeping the route resource flat (no nested waypoints).
    }

    /**
     * Updates the route's editable information from an update command (US-5 edit flow).
     *
     * <p>Name, departure time, vehicle assignment and direction are always replaced; the operating
     * days are only replaced when the command carries a non-null {@code serviceDays} list. The stop
     * sequence is NOT touched here — it is managed through the dedicated stop operations. The
     * lifecycle state is intentionally untouched: state transitions go through {@link #activate()}.</p>
     *
     * @param command the {@link UpdateRouteCommand} carrying the new values
     */
    public void updateInformation(UpdateRouteCommand command) {
        this.name = validateName(command.name());
        this.departureTime = DepartureTime.of(command.departureTime());
        this.vehicleId = command.vehicleId() == null ? null : new VehicleId(command.vehicleId());
        this.routeType = RouteType.fromNullable(command.routeType());
        if (command.serviceDays() != null) {
            this.serviceDays = new ServiceDays(command.serviceDays());
        }
    }

    /**
     * Replaces the whole stop sequence with the given stop data (bulk set), keeping the route as the
     * consistency boundary for its stops. Registers a single {@link StopCountUpdatedEvent}.
     *
     * @param newStops the replacement stop data, in the desired order
     */
    public void replaceStops(List<RouteStopData> newStops) {
        this.stops.clear();
        for (var stop : newStops) {
            this.stops.add(new Stop(stop.name(), new Coordinates(stop.latitude(), stop.longitude()), stop.order()));
        }
        registerDomainEvent(new StopCountUpdatedEvent(this.id, this.stops.size()));
    }

    /**
     * Adds a single stop to the route (PUML: addStop). Registers a {@link StopCountUpdatedEvent}.
     *
     * @param name        stop name
     * @param latitude    stop latitude
     * @param longitude   stop longitude
     * @param stopOrder   position within the route sequence
     * @return the newly added {@link Stop}
     */
    public Stop addStop(String name, double latitude, double longitude, int stopOrder) {
        var stop = new Stop(name, new Coordinates(latitude, longitude), stopOrder);
        this.stops.add(stop);
        registerDomainEvent(new StopCountUpdatedEvent(this.id, this.stops.size()));
        return stop;
    }

    /**
     * Activates the route (US-6).
     *
     * <p>Business rule: only a route currently in DRAFT may be activated; activating an already
     * active or inactive route is rejected. Registers a {@link RouteActivatedEvent} on success.</p>
     *
     * @throws IllegalStateException if the route is not in DRAFT state
     */
    public void activate() {
        if (this.routeState != RouteState.DRAFT) {
            throw new IllegalStateException("Only a route in DRAFT state can be activated");
        }
        this.routeState = RouteState.ACTIVE;
        registerDomainEvent(new RouteActivationFinalizedEvent(this.id));
    }

    /**
     * Indicates whether the route is currently active.
     *
     * @return true if the route state is ACTIVE
     */
    public boolean isActive() {
        return this.routeState == RouteState.ACTIVE;
    }

    /**
     * Returns the lowercase name of the current route state, matching the frontend contract.
     *
     * @return the route state name in lowercase (e.g. "active")
     */
    public String getStateName() {
        return this.routeState.name();
    }

    /**
     * Signals that this route has just been defined and persisted, registering the Event Storming
     * facts that became true at definition time: {@link RouteDefinedEvent} always, plus the
     * conditional facts {@link ServiceDaysDefinedEvent}, {@link DepartureTimeSetEvent} and
     * {@link VehicleAssignedToRouteEvent} (when a vehicle is assigned). Stop-count facts are emitted
     * by the stop operations (addStop / replaceStops), not at creation time.
     */
    public void onCreated() {
        registerDomainEvent(new RouteDefinedEvent(
                this.id,
                this.name,
                this.organizationId.organizationId()));
        registerDomainEvent(new ServiceDaysDefinedEvent(this.id, this.serviceDays.days()));
        registerDomainEvent(new DepartureTimeSetEvent(this.id, this.departureTime.asText()));
        if (this.vehicleId != null) {
            registerDomainEvent(new VehicleAssignedToRouteEvent(this.id, this.vehicleId.vehicleId()));
        }
    }

    /**
     * Assigns the persistence identity. Used by assemblers during reconstruction.
     *
     * @param id the identity to assign
     */
    public void setId(Long id) {
        this.id = id;
    }

    private static String validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Route name must not be null or blank");
        }
        return name;
    }
}
