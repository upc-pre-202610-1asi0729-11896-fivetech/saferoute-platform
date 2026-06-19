package com.acme.saferoute.platform.trip.domain.model.commands;

/**
 * Command expressing the intent to report an incident during a trip.
 *
 * @param tripId      identifier of the trip the incident occurred on
 * @param description human-readable incident description
 */
public record ReportIncidentCommand(
        Long tripId,
        String description) {
}
