package com.acme.saferoute.platform.fleet.domain.model.aggregates;

import com.acme.saferoute.platform.fleet.domain.model.commands.CreateVehicleCommand;
import com.acme.saferoute.platform.fleet.domain.model.commands.UpdateVehicleCommand;
import com.acme.saferoute.platform.fleet.domain.model.events.VehicleCreatedEvent;
import com.acme.saferoute.platform.fleet.domain.model.valueobjects.OrganizationId;
import com.acme.saferoute.platform.fleet.domain.model.valueobjects.VehicleStatus;
import com.acme.saferoute.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;

/**
 * Vehicle aggregate root.
 *
 * <p>Represents a vehicle belonging to an organization's fleet (US-2 context for assets).
 * A vehicle is identified by a unique plate and characterized by its model, passenger capacity and
 * operational {@link VehicleStatus}. It is an independent aggregate; routes reference it by identity
 * via the {@code VehicleId} value object rather than holding an object reference.</p>
 */
@Getter
public class Vehicle extends AbstractDomainAggregateRoot<Vehicle> {

    /**
     * Persistence identity. Assigned by the infrastructure layer; null until persisted.
     */
    private Long id;

    /**
     * Identifier of the organization that owns the vehicle.
     */
    private OrganizationId organizationId;

    /**
     * License plate, unique within the fleet.
     */
    private String plate;

    /**
     * Vehicle model.
     */
    private String model;

    /**
     * Passenger capacity; always positive.
     */
    private int capacity;

    /**
     * Operational status of the vehicle.
     */
    private VehicleStatus status;

    /**
     * Default constructor required for reconstruction from persistence.
     */
    public Vehicle() {
    }

    /**
     * Creates a new vehicle from a creation command.
     *
     * @param command the {@link CreateVehicleCommand} carrying the vehicle data
     */
    public Vehicle(CreateVehicleCommand command) {
        this.organizationId = new OrganizationId(command.organizationId());
        this.plate = validatePlate(command.plate());
        this.model = validateRequired(command.model(), "model");
        this.capacity = validateCapacity(command.capacity());
        this.status = VehicleStatus.fromNullable(command.status());
    }

    /**
     * Full reconstruction constructor used by the persistence assembler.
     *
     * @param id             persistence identity
     * @param organizationId owning organization identifier
     * @param plate          license plate
     * @param model          vehicle model
     * @param capacity       passenger capacity
     * @param status         operational status; null defaults to ACTIVE
     */
    public Vehicle(Long id, OrganizationId organizationId, String plate, String model,
                   int capacity, VehicleStatus status) {
        this.id = id;
        this.organizationId = organizationId;
        this.plate = plate;
        this.model = model;
        this.capacity = capacity;
        this.status = status == null ? VehicleStatus.ACTIVE : status;
    }

    /**
     * Updates the editable information of the vehicle from an update command.
     *
     * @param command the {@link UpdateVehicleCommand} carrying the new values
     */
    public void updateInformation(UpdateVehicleCommand command) {
        this.plate = validatePlate(command.plate());
        this.model = validateRequired(command.model(), "model");
        this.capacity = validateCapacity(command.capacity());
        this.status = VehicleStatus.fromNullable(command.status());
    }

    /**
     * Returns the status name, matching the front-end {@code status} string contract.
     *
     * @return the status name (e.g. "ACTIVE")
     */
    public String getStatusName() {
        return this.status.name();
    }

    /**
     * Signals that this vehicle has just been created and persisted, registering a
     * {@link VehicleCreatedEvent} for downstream consumers.
     */
    public void onCreated() {
        registerDomainEvent(new VehicleCreatedEvent(
                this.id,
                this.plate,
                this.organizationId.organizationId()));
    }

    /**
     * Assigns the persistence identity. Used by assemblers during reconstruction.
     *
     * @param id the identity to assign
     */
    public void setId(Long id) {
        this.id = id;
    }

    private static String validatePlate(String plate) {
        if (plate == null || plate.isBlank()) {
            throw new IllegalArgumentException("Vehicle plate must not be null or blank");
        }
        return plate.trim().toUpperCase();
    }

    private static String validateRequired(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Vehicle %s must not be null or blank".formatted(field));
        }
        return value;
    }

    private static int validateCapacity(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Vehicle capacity must be a positive number");
        }
        return capacity;
    }
}
