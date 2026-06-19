package com.acme.saferoute.platform.trip.infrastructure.persistence.jpa.adapters;

import com.acme.saferoute.platform.trip.domain.model.aggregates.Trip;
import com.acme.saferoute.platform.trip.domain.model.valueobjects.OrganizationId;
import com.acme.saferoute.platform.trip.domain.repositories.TripRepository;
import com.acme.saferoute.platform.trip.infrastructure.persistence.jpa.assemblers.TripPersistenceAssembler;
import com.acme.saferoute.platform.trip.infrastructure.persistence.jpa.repositories.TripPersistenceRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository adapter bridging the {@link TripRepository} domain port with Spring Data JPA.
 *
 * <p>Acts as the domain-event publishing boundary for the Trip context: events registered on the
 * aggregate during a mutation (boarding update, incident report, completion) are published after a
 * successful save, and the {@code TripStartedEvent} is registered and published once the JPA identity
 * is available for a newly started trip.</p>
 */
@Repository
public class TripRepositoryImpl implements TripRepository {

    private final TripPersistenceRepository tripPersistenceRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Creates the adapter with its Spring Data repository and the event publisher.
     *
     * @param tripPersistenceRepository the Spring Data JPA repository
     * @param eventPublisher            the application event publisher
     */
    public TripRepositoryImpl(TripPersistenceRepository tripPersistenceRepository,
                              ApplicationEventPublisher eventPublisher) {
        this.tripPersistenceRepository = tripPersistenceRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Trip save(Trip trip) {
        boolean isNew = trip.getId() == null;

        // Capture events registered during this unit of work (boarding, incident, completion).
        var pendingEvents = new ArrayList<>(trip.domainEvents());
        trip.clearDomainEvents();

        var savedEntity = tripPersistenceRepository.save(TripPersistenceAssembler.toPersistenceFromDomain(trip));
        var savedTrip = TripPersistenceAssembler.toDomainFromPersistence(savedEntity);

        if (isNew) {
            savedTrip.onStarted();
            pendingEvents.addAll(savedTrip.domainEvents());
            savedTrip.clearDomainEvents();
        }

        pendingEvents.forEach(eventPublisher::publishEvent);
        return savedTrip;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Trip> findById(Long id) {
        return tripPersistenceRepository.findById(id).map(TripPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trip> findAllByOrganizationId(OrganizationId organizationId) {
        return tripPersistenceRepository.findAllByOrganizationId(organizationId).stream()
                .map(TripPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public boolean existsById(Long id) {
        return tripPersistenceRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        tripPersistenceRepository.deleteById(id);
    }
}
