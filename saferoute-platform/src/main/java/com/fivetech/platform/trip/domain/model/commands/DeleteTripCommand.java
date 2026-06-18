package com.acme.saferoute.platform.trip.domain.model.commands;

/**
 * Command expressing the intent to delete a trip by its identifier.
 *
 * @param tripId identifier of the trip to delete
 */
public record DeleteTripCommand(Long tripId) {
}
