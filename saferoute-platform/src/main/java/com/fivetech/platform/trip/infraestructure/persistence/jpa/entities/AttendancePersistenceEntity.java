package com.acme.saferoute.platform.trip.infrastructure.persistence.jpa.entities;

import com.acme.saferoute.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import com.acme.saferoute.platform.trip.domain.model.valueobjects.BoardingState;
import com.acme.saferoute.platform.trip.domain.model.valueobjects.ChildId;
import com.acme.saferoute.platform.trip.infrastructure.persistence.jpa.converters.ChildIdConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * JPA persistence entity for attendances (table {@code attendances}).
 *
 * <p>Child persistence entity of {@link TripPersistenceEntity}, linked through a mandatory
 * {@code trip_id} foreign key. Records a child's boarding state during the trip.</p>
 */
@Entity
@Table(name = "attendances")
@Getter
@Setter
@NoArgsConstructor
public class AttendancePersistenceEntity extends AuditableAbstractPersistenceEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private TripPersistenceEntity trip;

    @Convert(converter = ChildIdConverter.class)
    @Column(name = "child_id", nullable = false)
    private ChildId childId;

    @Enumerated(EnumType.STRING)
    @Column(name = "boarding_state", nullable = false)
    private BoardingState boardingState;

    @Column(name = "boarded_at")
    private Instant boardedAt;
}
