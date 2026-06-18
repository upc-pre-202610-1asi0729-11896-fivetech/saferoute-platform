package com.fivetech.platform.fleet.infrastructure.persistence.jpa.entities;

import com.fivetech.platform.fleet.domain.model.valueobjects.*;
import com.fivetech.platform.fleet.infrastructure.persistence.jpa.converters.DepartureTimeConverter;
import com.fivetech.platform.fleet.infrastructure.persistence.jpa.converters.OrganizationIdConverter;
import com.fivetech.platform.fleet.infrastructure.persistence.jpa.converters.ServiceDaysConverter;
import com.fivetech.platform.fleet.infrastructure.persistence.jpa.converters.VehicleIdConverter;
import com.fivetech.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * JPA persistence entity for routes (table {@code routes}).
 *
 * <p>Holds the relational mapping for the {@code Route} aggregate. Value objects are mapped via
 * dedicated {@code AttributeConverter}s, the lifecycle state is stored as a string enum, and the
 * ordered stops are owned via a one-to-many association with cascade and orphan removal so that
 * the route remains the persistence boundary for its stops.</p>
 */
@Entity
@Table(name = "routes")
@Getter
@Setter
@NoArgsConstructor
public class RoutePersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(nullable = false)
    private String name;

    @Convert(converter = OrganizationIdConverter.class)
    @Column(name = "organization_id", nullable = false)
    private OrganizationId organizationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "route_state", nullable = false)
    private RouteState routeState;

    // Nullable on purpose: rows created before this column existed carry NULL, which the
    // persistence assembler maps to the OUTBOUND default.
    @Enumerated(EnumType.STRING)
    @Column(name = "route_type")
    private RouteType routeType;

    @Convert(converter = DepartureTimeConverter.class)
    @Column(name = "departure_time", nullable = false)
    private DepartureTime departureTime;

    @Convert(converter = ServiceDaysConverter.class)
    @Column(name = "service_days", nullable = false)
    private ServiceDays serviceDays;

    @Convert(converter = VehicleIdConverter.class)
    @Column(name = "vehicle_id")
    private VehicleId vehicleId;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StopPersistenceEntity> stops = new ArrayList<>();
}
