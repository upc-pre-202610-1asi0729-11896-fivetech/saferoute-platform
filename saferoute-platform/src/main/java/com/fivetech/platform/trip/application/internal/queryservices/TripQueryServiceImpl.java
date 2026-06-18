package com.acme.saferoute.platform.trip.application.internal.queryservices;

import com.acme.saferoute.platform.trip.application.queryservices.TripQueryService;
import com.acme.saferoute.platform.trip.domain.model.aggregates.Trip;
import com.acme.saferoute.platform.trip.domain.model.entities.Attendance;
import com.acme.saferoute.platform.trip.domain.model.entities.Incident;
import com.acme.saferoute.platform.trip.domain.model.queries.GetAllTripsByOrganizationIdQuery;
import com.acme.saferoute.platform.trip.domain.model.queries.GetAttendancesByTripIdQuery;
import com.acme.saferoute.platform.trip.domain.model.queries.GetIncidentsByTripIdQuery;
import com.acme.saferoute.platform.trip.domain.model.queries.GetTripByIdQuery;
import com.acme.saferoute.platform.trip.domain.model.valueobjects.OrganizationId;
import com.acme.saferoute.platform.trip.domain.repositories.TripRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Trip query service implementation.
 *
 * <p>Resolves Trip read models through the {@link TripRepository} port. Attendance and incident
 * collections are read from the owning trip aggregate, honoring the aggregate boundary.</p>
 */
@Service
public class TripQueryServiceImpl implements TripQueryService {

    private final TripRepository tripRepository;

    /**
     * Creates the service with its required repository port.
     *
     * @param tripRepository the trip repository port
     */
    public TripQueryServiceImpl(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    // inherited javadoc
    @Override
    public Optional<Trip> handle(GetTripByIdQuery query) {
        return tripRepository.findById(query.tripId());
    }

    // inherited javadoc
    @Override
    public List<Trip> handle(GetAllTripsByOrganizationIdQuery query) {
        return tripRepository.findAllByOrganizationId(new OrganizationId(query.organizationId()));
    }

    // inherited javadoc
    @Override
    public List<Attendance> handle(GetAttendancesByTripIdQuery query) {
        return tripRepository.findById(query.tripId())
                .map(Trip::getAttendances)
                .orElse(Collections.emptyList());
    }

    // inherited javadoc
    @Override
    public List<Incident> handle(GetIncidentsByTripIdQuery query) {
        return tripRepository.findById(query.tripId())
                .map(Trip::getIncidents)
                .orElse(Collections.emptyList());
    }
}
