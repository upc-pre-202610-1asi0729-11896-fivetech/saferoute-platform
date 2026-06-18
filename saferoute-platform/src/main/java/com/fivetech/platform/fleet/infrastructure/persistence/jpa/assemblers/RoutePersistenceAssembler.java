package com.fivetech.platform.fleet.infrastructure.persistence.jpa.assemblers;

import com.fivetech.platform.fleet.domain.model.aggregates.Route;
import com.fivetech.platform.fleet.domain.model.entities.Stop;
import com.fivetech.platform.fleet.domain.model.valueobjects.Coordinates;
import com.fivetech.platform.fleet.infrastructure.persistence.jpa.embeddables.CoordinatesPersistenceEmbeddable;
import com.fivetech.platform.fleet.infrastructure.persistence.jpa.entities.RoutePersistenceEntity;
import com.fivetech.platform.fleet.infrastructure.persistence.jpa.entities.StopPersistenceEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Static assembler that maps between the {@link Route} domain aggregate (with its {@link Stop}
 * children) and its {@link RoutePersistenceEntity} relational representation.
 *
 * <p>Keeps mapping logic out of both the domain and the persistence entities. New aggregates and
 * stops carry a {@code null} id so JPA can generate identities; existing ones keep their id so the
 * save becomes an update.</p>
 */
public final class RoutePersistenceAssembler {

    private RoutePersistenceAssembler() {
    }

    /**
     * Rebuilds a {@link Route} domain aggregate from its persistence entity.
     *
     * @param entity the route persistence entity (may be null)
     * @return the corresponding domain route, or null if {@code entity} is null
     */
    public static Route toDomainFromPersistence(RoutePersistenceEntity entity) {
        if (entity == null) {
            return null;
        }

        List<Stop> stops = new ArrayList<>();
        for (var stopEntity : entity.getStops()) {
            var coordinates = new Coordinates(
                    stopEntity.getCoordinates().getLatitude(),
                    stopEntity.getCoordinates().getLongitude());
            stops.add(new Stop(stopEntity.getId(), stopEntity.getName(), coordinates, stopEntity.getStopOrder()));
        }

        return new Route(
                entity.getId(),
                entity.getName(),
                entity.getOrganizationId(),
                entity.getRouteState(),
                entity.getRouteType(),
                entity.getDepartureTime(),
                entity.getServiceDays(),
                entity.getVehicleId(),
                stops);
    }

    /**
     * Maps a {@link Route} domain aggregate to a new persistence entity ready to be saved.
     *
     * @param route the domain route
     * @return the corresponding persistence entity, or null if {@code route} is null
     */
    public static RoutePersistenceEntity toPersistenceFromDomain(Route route) {
        if (route == null) {
            return null;
        }

        var entity = new RoutePersistenceEntity();
        if (route.getId() != null) {
            entity.setId(route.getId());
        }
        entity.setName(route.getName());
        entity.setOrganizationId(route.getOrganizationId());
        entity.setRouteState(route.getRouteState());
        entity.setRouteType(route.getRouteType());
        entity.setDepartureTime(route.getDepartureTime());
        entity.setServiceDays(route.getServiceDays());
        entity.setVehicleId(route.getVehicleId());

        List<StopPersistenceEntity> stopEntities = new ArrayList<>();
        for (var stop : route.getStops()) {
            var stopEntity = new StopPersistenceEntity();
            if (stop.getId() != null) {
                stopEntity.setId(stop.getId());
            }
            stopEntity.setRoute(entity);
            stopEntity.setName(stop.getName());
            stopEntity.setCoordinates(new CoordinatesPersistenceEmbeddable(
                    stop.getCoordinates().latitude(),
                    stop.getCoordinates().longitude()));
            stopEntity.setStopOrder(stop.getStopOrder());
            stopEntities.add(stopEntity);
        }
        entity.setStops(stopEntities);

        return entity;
    }
}
