package com.acme.saferoute.platform.fleet.infrastructure.persistence.jpa.entities;

import com.acme.saferoute.platform.fleet.domain.model.valueobjects.DriverId;
import com.acme.saferoute.platform.fleet.domain.model.valueobjects.RouteId;
import com.acme.saferoute.platform.fleet.infrastructure.persistence.jpa.converters.DriverIdConverter;
import com.acme.saferoute.platform.fleet.infrastructure.persistence.jpa.converters.RouteIdConverter;
import com.acme.saferoute.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * JPA persistence entity for assignments (table {@code assignments}).
 *
 * <p>Holds the relational mapping for the {@code Assignment} aggregate. The set of assigned
 * children is modeled as an element collection persisted in a dedicated child table
 * ({@code assignment_children}) with a foreign key back to the owning assignment, yielding a clean
 * relational structure while the domain keeps a typed list of child identifiers. A route has at
 * most one assignment, enforced by a unique constraint on {@code route_id}.</p>
 */
@Entity
@Table(
        name = "assignments",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_assignment_route",
                columnNames = {"route_id"}))
@Getter
@Setter
@NoArgsConstructor
public class AssignmentPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Convert(converter = RouteIdConverter.class)
    @Column(name = "route_id", nullable = false)
    private RouteId routeId;

    @Convert(converter = DriverIdConverter.class)
    @Column(name = "driver_id", nullable = false)
    private DriverId driverId;

    @ElementCollection
    @CollectionTable(
            name = "assignment_children",
            joinColumns = @JoinColumn(name = "assignment_id"))
    @Column(name = "child_id", nullable = false)
    private List<Long> childIds = new ArrayList<>();
}
