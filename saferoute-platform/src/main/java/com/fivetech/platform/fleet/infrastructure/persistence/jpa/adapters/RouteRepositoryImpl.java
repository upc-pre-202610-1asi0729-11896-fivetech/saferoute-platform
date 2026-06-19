package com.fivetech.platform.fleet.infrastructure.persistence.jpa.adapters;

import com.fivetech.platform.fleet.domain.model.aggregates.Route;
import com.fivetech.platform.fleet.domain.model.valueobjects.OrganizationId;
import com.fivetech.platform.fleet.domain.repositories.RouteRepository;
import com.fivetech.platform.fleet.infrastructure.persistence.jpa.assemblers.RoutePersistenceAssembler;
import com.fivetech.platform.fleet.infrastructure.persistence.jpa.repositories.RoutePersistenceRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository adapter bridging the {@link RouteRepository} domain port with Spring Data JPA.
 *
 * <p>Also acts as the domain-event publishing boundary: events registered on the aggregate during
 * a mutation (e.g. activation) are published after a successful save, and creation events are
 * registered and published once the JPA identity is available, via Spring's
 * {@link ApplicationEventPublisher}.</p>
 */
@Repository
public class RouteRepositoryImpl implements RouteRepository {

    private final RoutePersistenceRepository routePersistenceRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Creates the adapter with its Spring Data repository and the event publisher.
     *
     * @param routePersistenceRepository the Spring Data JPA repository
     * @param eventPublisher             the application event publisher
     */
    public RouteRepositoryImpl(RoutePersistenceRepository routePersistenceRepository,
                               ApplicationEventPublisher eventPublisher) {
        this.routePersistenceRepository = routePersistenceRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Route save(Route route) {
        boolean isNew = route.getId() == null;

        // Capture events registered during this unit of work (e.g. RouteActivatedEvent).
        var pendingEvents = new ArrayList<>(route.domainEvents());
        route.clearDomainEvents();

        var savedEntity = routePersistenceRepository.save(RoutePersistenceAssembler.toPersistenceFromDomain(route));
        var savedRoute = RoutePersistenceAssembler.toDomainFromPersistence(savedEntity);

        if (isNew) {
            savedRoute.onCreated();
            pendingEvents.addAll(savedRoute.domainEvents());
            savedRoute.clearDomainEvents();
        }

        pendingEvents.forEach(eventPublisher::publishEvent);
        return savedRoute;
    }

    @Override
    public Optional<Route> findById(Long id) {
        return routePersistenceRepository.findById(id).map(RoutePersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<Route> findAllByOrganizationId(OrganizationId organizationId) {
        return routePersistenceRepository.findAllByOrganizationId(organizationId).stream()
                .map(RoutePersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public boolean existsById(Long id) {
        return routePersistenceRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        routePersistenceRepository.deleteById(id);
    }
}
