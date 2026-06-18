package com.acme.saferoute.platform.fleet.infrastructure.persistence.jpa.adapters;

import com.acme.saferoute.platform.fleet.domain.model.aggregates.Assignment;
import com.acme.saferoute.platform.fleet.domain.model.valueobjects.RouteId;
import com.acme.saferoute.platform.fleet.domain.repositories.AssignmentRepository;
import com.acme.saferoute.platform.fleet.infrastructure.persistence.jpa.assemblers.AssignmentPersistenceAssembler;
import com.acme.saferoute.platform.fleet.infrastructure.persistence.jpa.repositories.AssignmentPersistenceRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Repository adapter bridging the {@link AssignmentRepository} domain port with Spring Data JPA.
 *
 * <p>Publishes the {@code AssignmentCreatedEvent} after a new assignment is persisted, enabling
 * downstream contexts (e.g. Notifications) to alert the assigned driver (US-6 S1).</p>
 */
@Repository
public class AssignmentRepositoryImpl implements AssignmentRepository {

    private final AssignmentPersistenceRepository assignmentPersistenceRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Creates the adapter with its Spring Data repository and the event publisher.
     *
     * @param assignmentPersistenceRepository the Spring Data JPA repository
     * @param eventPublisher                  the application event publisher
     */
    public AssignmentRepositoryImpl(AssignmentPersistenceRepository assignmentPersistenceRepository,
                                    ApplicationEventPublisher eventPublisher) {
        this.assignmentPersistenceRepository = assignmentPersistenceRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Assignment save(Assignment assignment) {
        boolean isNew = assignment.getId() == null;

        var pendingEvents = new ArrayList<>(assignment.domainEvents());
        assignment.clearDomainEvents();

        var savedEntity = assignmentPersistenceRepository.save(
                AssignmentPersistenceAssembler.toPersistenceFromDomain(assignment));
        var savedAssignment = AssignmentPersistenceAssembler.toDomainFromPersistence(savedEntity);

        if (isNew) {
            savedAssignment.onCreated();
            pendingEvents.addAll(savedAssignment.domainEvents());
            savedAssignment.clearDomainEvents();
        }

        pendingEvents.forEach(eventPublisher::publishEvent);
        return savedAssignment;
    }

    @Override
    public Optional<Assignment> findByRouteId(RouteId routeId) {
        return assignmentPersistenceRepository.findByRouteId(routeId)
                .map(AssignmentPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public boolean existsByRouteId(RouteId routeId) {
        return assignmentPersistenceRepository.existsByRouteId(routeId);
    }

    @Override
    @Transactional
    public void deleteByRouteId(RouteId routeId) {
        assignmentPersistenceRepository.deleteByRouteId(routeId);
    }
}
