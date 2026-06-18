package com.acme.saferoute.platform.fleet.domain.model.aggregates;

import com.acme.saferoute.platform.fleet.domain.model.commands.CreateAssignmentCommand;
import com.acme.saferoute.platform.fleet.domain.model.events.StudentAssignedToRouteEvent;
import com.acme.saferoute.platform.fleet.domain.model.valueobjects.ChildId;
import com.acme.saferoute.platform.fleet.domain.model.valueobjects.DriverId;
import com.acme.saferoute.platform.fleet.domain.model.valueobjects.RouteId;
import com.acme.saferoute.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Assignment aggregate root.
 *
 * <p>Represents the operational staffing of a route (US-6: "Asignación de Roles"): it links a
 * driver and the set of children transported to a specific route. References to the route,
 * driver and children are held by identity (value objects), keeping the Assignment aggregate
 * decoupled from the Route, Stakeholder driver and child aggregates.</p>
 */
@Getter
public class Assignment extends AbstractDomainAggregateRoot<Assignment> {

    /**
     * Persistence identity. Assigned by the infrastructure layer; null until persisted.
     */
    private Long id;

    /**
     * Identifier of the route being staffed.
     */
    private RouteId routeId;

    /**
     * Identifier of the assigned driver.
     */
    private DriverId driverId;

    /**
     * Identifiers of the children assigned to the route.
     */
    private final List<ChildId> childIds = new ArrayList<>();

    /**
     * Default constructor required for reconstruction from persistence.
     */
    public Assignment() {
    }

    /**
     * Creates a new assignment from a creation command.
     *
     * <p>A driver is mandatory (it is the essence of staffing a route, US-6); the children list may
     * be empty so an administrator can assign the driver first and enroll children later.</p>
     *
     * @param command the {@link CreateAssignmentCommand} carrying the assignment data
     */
    public Assignment(CreateAssignmentCommand command) {
        this.routeId = new RouteId(command.routeId());
        this.driverId = new DriverId(command.driverId());
        if (command.childIds() != null) {
            command.childIds().forEach(childId -> this.childIds.add(new ChildId(childId)));
        }
    }

    /**
     * Replaces the staffing of this assignment: the driver and the full children list.
     *
     * @param driverId the new driver identifier
     * @param childIds the replacement children identifiers (null treated as empty)
     */
    public void updateStaffing(DriverId driverId, List<ChildId> childIds) {
        this.driverId = Objects.requireNonNull(driverId, "driverId must not be null");
        this.childIds.clear();
        if (childIds != null) {
            this.childIds.addAll(childIds);
        }
    }

    /**
     * Full reconstruction constructor used by the persistence assembler.
     *
     * @param id       persistence identity
     * @param routeId  staffed route identifier
     * @param driverId assigned driver identifier
     * @param childIds assigned children identifiers
     */
    public Assignment(Long id, RouteId routeId, DriverId driverId, List<ChildId> childIds) {
        this.id = id;
        this.routeId = routeId;
        this.driverId = driverId;
        if (childIds != null) {
            this.childIds.addAll(childIds);
        }
    }

    /**
     * Signals that students (and a driver) have just been assigned to a route and persisted,
     * registering a {@link StudentAssignedToRouteEvent} (Event Storming: <b>StudentAssignedToRoute</b>)
     * so downstream contexts (e.g. Notifications) can alert the driver (US-6 S1).
     */
    public void onCreated() {
        registerDomainEvent(new StudentAssignedToRouteEvent(
                this.id,
                this.routeId.routeId(),
                this.driverId.driverId(),
                this.childIds.stream().map(ChildId::childId).toList()));
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
