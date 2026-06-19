package com.fivetech.platform.fleet.interfaces.rest.transform;

import com.fivetech.platform.fleet.domain.model.aggregates.Route;
import com.fivetech.platform.fleet.interfaces.rest.resources.RouteResource;
import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * Assembler converting a {@link Route} aggregate into the flat, front-end compatible
 * {@link RouteResource}.
 *
 * <p>Recomposes the denormalized route resource from the domain: {@code routeState} becomes
 * {@code status}, {@code routeType} becomes {@code type}, {@code departureTime} becomes
 * {@code scheduledStartTime}. Stops are NOT included here — they are served by the dedicated
 * {@code /routes/{id}/stops} endpoints. The {@code vehiclePlate}, driver and student data are
 * supplied by the caller from the sibling Vehicle / Assignment aggregates.</p>
 */
public final class RouteResourceFromEntityAssembler {

    private RouteResourceFromEntityAssembler() {
    }

    /**
     * Builds the flat route resource from the aggregate plus the staffing data resolved by the
     * caller from sibling aggregates.
     *
     * @param route        the route aggregate
     * @param vehiclePlate the plate of the assigned vehicle, or null/empty if none
     * @param driverId     the assigned driver id from the route's assignment, or null if unstaffed
     * @param studentIds   the assigned children ids from the route's assignment (null treated as empty)
     * @return the flat route resource
     */
    public static RouteResource toResourceFromEntity(Route route,
                                                     @Nullable String vehiclePlate,
                                                     @Nullable Long driverId,
                                                     @Nullable List<Long> studentIds) {
        return new RouteResource(
                route.getId(),
                route.getName(),
                route.getRouteType().name(),
                route.getStateName(),
                driverId,
                "",
                route.getVehicleId() == null ? null : route.getVehicleId().vehicleId(),
                vehiclePlate == null ? "" : vehiclePlate,
                studentIds == null ? List.of() : studentIds,
                route.getDepartureTime().asText(),
                route.getOrganizationId().organizationId());
    }
}
