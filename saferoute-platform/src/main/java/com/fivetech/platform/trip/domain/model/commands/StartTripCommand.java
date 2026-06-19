package com.acme.saferoute.platform.trip.domain.model.commands;

/**
 * Command expressing the intent to start a trip (US-10: "Inicio de Trayecto").
 *
 * @param organizationId identifier of the owning organization
 * @param routeId        identifier of the route being executed
 * @param driverId       identifier of the driver starting the trip
 */
public record StartTripCommand(
        Long organizationId,
        Long routeId,
        Long driverId) {
}
