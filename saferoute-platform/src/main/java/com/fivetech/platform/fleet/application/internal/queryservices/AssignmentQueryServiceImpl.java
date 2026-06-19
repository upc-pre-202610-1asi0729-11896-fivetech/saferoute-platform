package com.acme.saferoute.platform.fleet.application.internal.queryservices;

import com.acme.saferoute.platform.fleet.application.queryservices.AssignmentQueryService;
import com.acme.saferoute.platform.fleet.domain.model.aggregates.Assignment;
import com.acme.saferoute.platform.fleet.domain.model.queries.GetAssignmentByRouteIdQuery;
import com.acme.saferoute.platform.fleet.domain.model.valueobjects.RouteId;
import com.acme.saferoute.platform.fleet.domain.repositories.AssignmentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Assignment query service implementation.
 *
 * <p>Resolves Fleet assignment read models through the {@link AssignmentRepository} port.</p>
 */
@Service
public class AssignmentQueryServiceImpl implements AssignmentQueryService {

    private final AssignmentRepository assignmentRepository;

    /**
     * Creates the service with its required repository port.
     *
     * @param assignmentRepository the assignment repository port
     */
    public AssignmentQueryServiceImpl(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    // inherited javadoc
    @Override
    public Optional<Assignment> handle(GetAssignmentByRouteIdQuery query) {
        return assignmentRepository.findByRouteId(new RouteId(query.routeId()));
    }
}
