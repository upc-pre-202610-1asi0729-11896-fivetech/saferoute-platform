package com.acme.saferoute.platform.fleet.infrastructure.persistence.jpa.assemblers;

import com.acme.saferoute.platform.fleet.domain.model.aggregates.Assignment;
import com.acme.saferoute.platform.fleet.domain.model.valueobjects.ChildId;
import com.acme.saferoute.platform.fleet.infrastructure.persistence.jpa.entities.AssignmentPersistenceEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Static assembler that maps between the {@link Assignment} domain aggregate and its
 * {@link AssignmentPersistenceEntity} relational representation.
 *
 * <p>The domain holds a typed list of {@link ChildId} value objects; the persistence entity stores
 * the raw {@code Long} identifiers in the {@code assignment_children} collection table. This
 * assembler translates between the two forms.</p>
 */
public final class AssignmentPersistenceAssembler {

    private AssignmentPersistenceAssembler() {
    }

    /**
     * Rebuilds an {@link Assignment} domain aggregate from its persistence entity.
     *
     * @param entity the assignment persistence entity (may be null)
     * @return the corresponding domain assignment, or null if {@code entity} is null
     */
    public static Assignment toDomainFromPersistence(AssignmentPersistenceEntity entity) {
        if (entity == null) {
            return null;
        }
        List<ChildId> childIds = entity.getChildIds().stream()
                .map(ChildId::new)
                .toList();
        return new Assignment(
                entity.getId(),
                entity.getRouteId(),
                entity.getDriverId(),
                childIds);
    }

    /**
     * Maps an {@link Assignment} domain aggregate to a new persistence entity ready to be saved.
     *
     * @param assignment the domain assignment
     * @return the corresponding persistence entity, or null if {@code assignment} is null
     */
    public static AssignmentPersistenceEntity toPersistenceFromDomain(Assignment assignment) {
        if (assignment == null) {
            return null;
        }
        var entity = new AssignmentPersistenceEntity();
        if (assignment.getId() != null) {
            entity.setId(assignment.getId());
        }
        entity.setRouteId(assignment.getRouteId());
        entity.setDriverId(assignment.getDriverId());
        List<Long> childIds = new ArrayList<>();
        assignment.getChildIds().forEach(childId -> childIds.add(childId.childId()));
        entity.setChildIds(childIds);
        return entity;
    }
}
