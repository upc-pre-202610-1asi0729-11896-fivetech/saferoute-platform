package com.acme.saferoute.platform.trip.domain.model.events;

/**
 * Domain event published when an incident is reported during a trip.
 *
 * <p>Enables the Notifications context to raise alerts to administrators and affected parents.</p>
 *
 * @param tripId      identity of the trip the incident was reported on
 * @param description incident description
 */
public record IncidentReportedEvent(
        Long tripId,
        String description) {
}
