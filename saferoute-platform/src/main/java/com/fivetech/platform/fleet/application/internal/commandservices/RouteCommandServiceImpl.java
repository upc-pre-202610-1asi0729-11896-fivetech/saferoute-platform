package com.fivetech.platform.fleet.application.internal.commandservices;

import com.fivetech.platform.fleet.application.commandservices.RouteCommandService;
import com.fivetech.platform.fleet.domain.model.aggregates.Route;
import com.fivetech.platform.fleet.domain.model.commands.ActivateRouteCommand;
import com.fivetech.platform.fleet.domain.model.commands.AddStopToRouteCommand;
import com.fivetech.platform.fleet.domain.model.commands.CreateRouteCommand;
import com.fivetech.platform.fleet.domain.model.commands.DeleteRouteCommand;
import com.fivetech.platform.fleet.domain.model.commands.ReplaceRouteStopsCommand;
import com.fivetech.platform.fleet.domain.model.commands.UpdateRouteCommand;
import com.fivetech.platform.fleet.domain.model.valueobjects.RouteId;
import com.fivetech.platform.fleet.domain.repositories.AssignmentRepository;
import com.fivetech.platform.fleet.domain.repositories.RouteRepository;
import com.fivetech.platform.shared.application.result.ApplicationError;
import com.fivetech.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;

/**
 * Route command service implementation.
 *
 * <p>Orchestrates route write use cases: it loads/creates the {@link Route} aggregate, delegates
 * business behavior and invariants to the aggregate, persists through the {@link RouteRepository}
 * port, and translates outcomes into a {@link Result}. Domain exceptions ({@code IllegalArgumentException},
 * {@code IllegalStateException}) are mapped to structured {@link ApplicationError} failures.</p>
 *
 * <p>The {@link AssignmentRepository} dependency (same bounded context) is used only to remove the
 * orphaned assignment when its route is deleted; assignments reference routes by identity, so no
 * database cascade covers that cleanup.</p>
 */
@Service
public class RouteCommandServiceImpl implements RouteCommandService {

    private final RouteRepository routeRepository;
    private final AssignmentRepository assignmentRepository;

    /**
     * Creates the service with its required repository ports.
     *
     * @param routeRepository      the route repository port
     * @param assignmentRepository the assignment repository port (orphan cleanup on route deletion)
     */
    public RouteCommandServiceImpl(RouteRepository routeRepository, AssignmentRepository assignmentRepository) {
        this.routeRepository = routeRepository;
        this.assignmentRepository = assignmentRepository;
    }

    // inherited javadoc
    @Override
    public Result<Route, ApplicationError> handle(CreateRouteCommand command) {
        try {
            var route = new Route(command);
            var savedRoute = routeRepository.save(route);
            return Result.success(savedRoute);
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("Route", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("Route creation", e.getMessage()));
        }
    }

    // inherited javadoc
    @Override
    public Result<Route, ApplicationError> handle(AddStopToRouteCommand command) {
        try {
            var routeOptional = routeRepository.findById(command.routeId());
            if (routeOptional.isEmpty()) {
                return Result.failure(ApplicationError.notFound("Route", String.valueOf(command.routeId())));
            }
            var route = routeOptional.get();
            route.addStop(command.name(), command.latitude(), command.longitude(), command.stopOrder());
            var savedRoute = routeRepository.save(route);
            return Result.success(savedRoute);
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("Stop", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("Add stop to route", e.getMessage()));
        }
    }

    // inherited javadoc
    @Override
    public Result<Route, ApplicationError> handle(ReplaceRouteStopsCommand command) {
        try {
            var routeOptional = routeRepository.findById(command.routeId());
            if (routeOptional.isEmpty()) {
                return Result.failure(ApplicationError.notFound("Route", String.valueOf(command.routeId())));
            }
            var route = routeOptional.get();
            route.replaceStops(command.stops() == null ? java.util.List.of() : command.stops());
            var savedRoute = routeRepository.save(route);
            return Result.success(savedRoute);
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("Stop", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("Replace route stops", e.getMessage()));
        }
    }

    // inherited javadoc
    @Override
    public Result<Route, ApplicationError> handle(ActivateRouteCommand command) {
        try {
            var routeOptional = routeRepository.findById(command.routeId());
            if (routeOptional.isEmpty()) {
                return Result.failure(ApplicationError.notFound("Route", String.valueOf(command.routeId())));
            }
            var route = routeOptional.get();
            route.activate();
            var savedRoute = routeRepository.save(route);
            return Result.success(savedRoute);
        } catch (IllegalStateException e) {
            return Result.failure(ApplicationError.businessRuleViolation("Route activation", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("Route activation", e.getMessage()));
        }
    }

    // inherited javadoc
    @Override
    public Result<Route, ApplicationError> handle(UpdateRouteCommand command) {
        try {
            var routeOptional = routeRepository.findById(command.routeId());
            if (routeOptional.isEmpty()) {
                return Result.failure(ApplicationError.notFound("Route", String.valueOf(command.routeId())));
            }
            var route = routeOptional.get();
            route.updateInformation(command);
            var savedRoute = routeRepository.save(route);
            return Result.success(savedRoute);
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("Route", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("Route update", e.getMessage()));
        }
    }

    // inherited javadoc
    @Override
    public Result<Long, ApplicationError> handle(DeleteRouteCommand command) {
        try {
            if (!routeRepository.existsById(command.routeId())) {
                return Result.failure(ApplicationError.notFound("Route", String.valueOf(command.routeId())));
            }
            // Remove the orphaned assignment first: it references the route by identity only.
            assignmentRepository.deleteByRouteId(new RouteId(command.routeId()));
            routeRepository.deleteById(command.routeId());
            return Result.success(command.routeId());
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("Route deletion", e.getMessage()));
        }
    }
}
