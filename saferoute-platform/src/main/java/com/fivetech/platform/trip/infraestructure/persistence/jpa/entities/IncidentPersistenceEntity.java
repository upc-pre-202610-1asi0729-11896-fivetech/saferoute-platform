package com.acme.saferoute.platform.trip.infrastructure.persistence.jpa.entities;

import com.acme.saferoute.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * JPA persistence entity for incidents (table {@code incidents}).
 *
 * <p>Child persistence entity of {@link TripPersistenceEntity}, linked through a mandatory
 * {@code trip_id} foreign key. Captures a free-text incident reported during the trip.</p>
 */
@Entity
@Table(name = "incidents")
@Getter
@Setter
@NoArgsConstructor
public class IncidentPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private TripPersistenceEntity trip;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(name = "reported_at", nullable = false)
    private Instant reportedAt;
}
