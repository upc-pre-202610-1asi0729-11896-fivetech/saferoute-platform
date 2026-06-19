package com.acme.saferoute.platform.trip.domain.model.entities;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * Incident domain entity.
 *
 * <p>Represents an event of note reported during a trip (e.g. a delay, mechanical issue). It is a
 * child entity of the {@code Trip} aggregate, capturing a free-text description and the moment it
 * was reported.</p>
 */
@Getter
public class Incident {

    /**
     * Persistence identity of the incident. Assigned by the infrastructure layer.
     */
    @Setter
    private Long id;

    /**
     * Human-readable description of the incident.
     */
    private final String description;

    /**
     * Timestamp at which the incident was reported.
     */
    private final Instant reportedAt;

    /**
     * Domain constructor for a brand-new incident, stamping the current time.
     *
     * @param description the incident description
     */
    public Incident(String description) {
        this.description = validateDescription(description);
        this.reportedAt = Instant.now();
    }

    /**
     * Full reconstruction constructor used by the persistence assembler.
     *
     * @param id          persistence identity
     * @param description incident description
     * @param reportedAt  report timestamp
     */
    public Incident(Long id, String description, Instant reportedAt) {
        this.id = id;
        this.description = description;
        this.reportedAt = reportedAt;
    }

    private static String validateDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Incident description must not be null or blank");
        }
        return description;
    }
}
