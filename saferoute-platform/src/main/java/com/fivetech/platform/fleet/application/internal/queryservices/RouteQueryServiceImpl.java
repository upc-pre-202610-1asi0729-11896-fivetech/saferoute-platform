package com.fivetech.platform.fleet.application.internal.queryservices;

import com.fivetech.platform.fleet.application.queryservices.RouteQueryService;
import com.fivetech.platform.fleet.domain.model.aggregates.Route;
import com.fivetech.platform.fleet.domain.model.entities.Stop;
import com.fivetech.platform.fleet.domain.model.queries.GetAllRoutesByOrganizationIdQuery;
import com.fivetech.platform.fleet.domain.model.queries.GetRouteByIdQuery;
import com.fivetech.platform.fleet.domain.model.queries.GetStopsByRouteIdQuery;
import com.fivetech.platform.fleet.domain.model.valueobjects.OrganizationId;
import com.fivetech.platform.fleet.domain.repositories.RouteRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Route query service implementation.
 *
 * <p>Resolves Fleet route read models through the {@link RouteRepository} port. Stops are read
 * from the owning route aggregate and returned ordered by their {@code stopOrder}.</p>
 */
@Service
public class RouteQueryServiceImpl implements RouteQueryService {

    private final RouteRepository routeRepository;

    /**
     * Creates the service with its required repository port.
     *
     * @param routeRepository the route repository port
     */
    public RouteQueryServiceImpl(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    // inherited javadoc
    @Override
    public Optional<Route> handle(GetRouteByIdQuery query) {
        return routeRepository.findById(query.routeId());
    }

    // inherited javadoc
    @Override
    public List<Route> handle(GetAllRoutesByOrganizationIdQuery query) {
        return routeRepository.findAllByOrganizationId(new OrganizationId(query.organizationId()));
    }

    // inherited javadoc
    @Override
    public List<Stop> handle(GetStopsByRouteIdQuery query) {
        return routeRepository.findById(query.routeId())
                .map(route -> route.getStops().stream()
                        .sorted(Comparator.comparingInt(Stop::getStopOrder))
                        .toList())
                .orElse(Collections.emptyList());
    }
}
