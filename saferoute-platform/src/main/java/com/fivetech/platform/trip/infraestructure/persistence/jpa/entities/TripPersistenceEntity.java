package com.acme.saferoute.platform.trip.infrastructure.persistence.jpa.entities;

import com.acme.saferoute.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import com.acme.saferoute.platform.trip.domain.model.valueobjects.DriverId;
import com.acme.saferoute.platform.trip.domain.model.valueobjects.OrganizationId;
import com.acme.saferoute.platform.trip.domain.model.valueobjects.RouteId;
import com.acme.saferoute.platform.trip.domain.model.valueobjects.TripState;
import com.acme.saferoute.platform.trip.infrastructure.persistence.jpa.converters.DriverIdConverter;
import com.acme.saferoute.platform.trip.infrastructure.persistence.jpa.converters.OrganizationIdConverter;
import com.acme.saferoute.platform.trip.infrastructure.persistence.jpa.converters.RouteIdConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA persistence entity for trips (table {@code trips}).
 *
 * <p>Holds the relational mapping for the {@code Trip} aggregate. Identifier value objects are mapped
 * via dedicated converters, the lifecycle state is stored as a string enum, and the attendance and
 * incident children are owned via cascading one-to-many associations so the trip remains the
 * persistence boundary for its children.</p>
 */
@Entity
@Table(name = "trips")
@Getter
@Setter
@NoArgsConstructor
public class TripPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Convert(converter = OrganizationIdConverter.class)
    @Column(name = "organization_id", nullable = false)
    private OrganizationId organizationId;

    @Convert(converter = RouteIdConverter.class)
    @Column(name = "route_id", nullable = false)
    private RouteId routeId;

    @Convert(converter = DriverIdConverter.class)
    @Column(name = "driver_id", nullable = false)
    private DriverId driverId;

    @Enumerated(EnumType.STRING)
    @Column(name = "trip_state", nullable = false)
    private TripState tripState;

    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AttendancePersistenceEntity> attendances = new ArrayList<>();

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IncidentPersistenceEntity> incidents = new ArrayList<>();
}
