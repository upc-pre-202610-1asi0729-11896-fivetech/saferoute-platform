package com.fivetech.platform.fleet.infrastructure.persistence.jpa.entities;

import com.acme.saferoute.platform.fleet.domain.model.valueobjects.OrganizationId;
import com.fivetech.platform.fleet.domain.model.valueobjects.VehicleStatus;
import com.acme.saferoute.platform.fleet.infrastructure.persistence.jpa.converters.OrganizationIdConverter;
import com.acme.saferoute.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JPA persistence entity for vehicles (table {@code vehicles}).
 *
 * <p>Holds the relational mapping for the {@code Vehicle} aggregate. The plate is unique within
 * the scope of an organization, enforced by a composite unique constraint.</p>
 */
@Entity
@Table(
        name = "vehicles",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_vehicle_organization_plate",
                columnNames = {"organization_id", "plate"}))
@Getter
@Setter
@NoArgsConstructor
public class VehiclePersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Convert(converter = OrganizationIdConverter.class)
    @Column(name = "organization_id", nullable = false)
    private OrganizationId organizationId;

    @Column(nullable = false)
    private String plate;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private int capacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private VehicleStatus status;
}
