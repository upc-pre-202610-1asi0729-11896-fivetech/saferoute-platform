package com.fivetech.platform.fleet.infrastructure.persistence.jpa.entities;

import com.fivetech.platform.fleet.infrastructure.persistence.jpa.embeddables.CoordinatesPersistenceEmbeddable;
import com.fivetech.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JPA persistence entity for stops (table {@code stops}).
 *
 * <p>Child persistence entity of {@link RoutePersistenceEntity}, linked through a mandatory
 * {@code route_id} foreign key. The geographic position is mapped as an embedded
 * {@link CoordinatesPersistenceEmbeddable}.</p>
 */
@Entity
@Table(name = "stops")
@Getter
@Setter
@NoArgsConstructor
public class StopPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private RoutePersistenceEntity route;

    @Column(nullable = false)
    private String name;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "latitude", nullable = false)),
            @AttributeOverride(name = "longitude", column = @Column(name = "longitude", nullable = false))})
    private CoordinatesPersistenceEmbeddable coordinates;

    @Column(name = "stop_order", nullable = false)
    private int stopOrder;
}
