package com.acme.saferoute.platform.fleet.application.internal.commandservices;

import com.acme.saferoute.platform.fleet.application.commandservices.AssignmentCommandService;
import com.acme.saferoute.platform.fleet.domain.model.aggregates.Assignment;
import com.acme.saferoute.platform.fleet.domain.model.commands.CreateAssignmentCommand;
import com.acme.saferoute.platform.fleet.domain.model.commands.UpdateAssignmentCommand;
import com.acme.saferoute.platform.fleet.domain.model.valueobjects.ChildId;
import com.acme.saferoute.platform.fleet.domain.model.valueobjects.DriverId;
import com.acme.saferoute.platform.fleet.domain.model.valueobjects.RouteId;
import com.acme.saferoute.platform.fleet.domain.repositories.AssignmentRepository;
import com.acme.saferoute.platform.fleet.domain.repositories.RouteRepository;
import com.acme.saferoute.platform.shared.application.result.ApplicationError;
import com.acme.saferoute.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Assignment command service implementation.
 *
 * <p>Validates that the target route exists and is not already staffed before delegating
 * assignment creation to the {@link Assignment} aggregate. Depends on the {@link RouteRepository}
 * port only to verify route existence by identity, which does not break aggregate boundaries
 * (no cross-aggregate object reference is created).</p>
 */
@Service
public class AssignmentCommandServiceImpl implements AssignmentCommandService {

    private final AssignmentRepository assignmentRepository;
    private final RouteRepository routeRepository;

    /**
     * Creates the service with its required repository ports.
     *
     * @param assignmentRepository the assignment repository port
     * @param routeRepository      the route repository port (used for existence validation only)
     */
    public AssignmentCommandServiceImpl(AssignmentRepository assignmentRepository, RouteRepository routeRepository) {
        this.assignmentRepository = assignmentRepository;
        this.routeRepository = routeRepository;
    }

    // inherited javadoc
    @Override
    public Result<Assignment, ApplicationError> handle(CreateAssignmentCommand command) {
        try {
            if (command.routeId() == null || routeRepository.findById(command.routeId()).isEmpty()) {
                return Result.failure(ApplicationError.notFound("Route", String.valueOf(command.routeId())));
            }

            var routeId = new RouteId(command.routeId());
            if (assignmentRepository.existsByRouteId(routeId)) {
                return Result.failure(ApplicationError.conflict(
                        "Assignment",
                        "Route '%s' already has an assignment".formatted(command.routeId())));
            }

            var assignment = new Assignment(command);
            var savedAssignment = assignmentRepository.save(assignment);
            return Result.success(savedAssignment);
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("Assignment", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("Assignment creation", e.getMessage()));
        }
    }

    // inherited javadoc
    @Override
    public Result<Assignment, ApplicationError> handle(UpdateAssignmentCommand command) {
        try {
            var assignmentOptional = assignmentRepository.findByRouteId(new RouteId(command.routeId()));
            if (assignmentOptional.isEmpty()) {
                return Result.failure(ApplicationError.notFound("Assignment", String.valueOf(command.routeId())));
            }

            var assignment = assignmentOptional.get();
            List<ChildId> childIds = command.childIds() == null
                    ? List.of()
                    : command.childIds().stream().map(ChildId::new).toList();
            assignment.updateStaffing(new DriverId(command.driverId()), childIds);
            var savedAssignment = assignmentRepository.save(assignment);
            return Result.success(savedAssignment);
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("Assignment", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("Assignment update", e.getMessage()));
        }
    }
}
