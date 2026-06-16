package com.acme.saferoute.platform.fleet.domain.model.queries;

/**
 * Query to retrieve a single vehicle by its unique identifier.
 *
 * @param vehicleId the vehicle identifier
 */
public record GetVehicleByIdQuery(Long vehicleId) {
}
