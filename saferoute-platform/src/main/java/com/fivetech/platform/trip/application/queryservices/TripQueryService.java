package com.acme.saferoute.platform.trip.application.queryservices;

import com.acme.saferoute.platform.trip.domain.model.aggregates.Trip;
import com.acme.saferoute.platform.trip.domain.model.entities.Attendance;
import com.acme.saferoute.platform.trip.domain.model.entities.Incident;
import com.acme.saferoute.platform.trip.domain.model.queries.GetAllTripsByOrganizationIdQuery;
import com.acme.saferoute.platform.trip.domain.model.queries.GetAttendancesByTripIdQuery;
import com.acme.saferoute.platform.trip.domain.model.queries.GetIncidentsByTripIdQuery;
import com.acme.saferoute.platform.trip.domain.model.queries.GetTripByIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Application service contract for Trip read operations within the Trip bounded context.
 */
public interface TripQueryService {

    /**
     * Resolves a trip by its identity.
     *
     * @param query the {@link GetTripByIdQuery}
     * @return the trip if found, otherwise empty
     */
    Optional<Trip> handle(GetTripByIdQuery query);

    /**
     * Resolves all trips for an organization.
     *
     * @param query the {@link GetAllTripsByOrganizationIdQuery}
     * @return the matching trips (possibly empty)
     */
    List<Trip> handle(GetAllTripsByOrganizationIdQuery query);

    /**
     * Resolves the attendance records of a trip.
     *
     * @param query the {@link GetAttendancesByTripIdQuery}
     * @return the trip's attendance records (possibly empty)
     */
    List<Attendance> handle(GetAttendancesByTripIdQuery query);

    /**
     * Resolves the incidents reported on a trip.
     *
     * @param query the {@link GetIncidentsByTripIdQuery}
     * @return the trip's incidents (possibly empty)
     */
    List<Incident> handle(GetIncidentsByTripIdQuery query);
}
