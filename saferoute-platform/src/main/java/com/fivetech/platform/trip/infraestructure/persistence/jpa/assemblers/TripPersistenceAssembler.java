package com.acme.saferoute.platform.trip.infrastructure.persistence.jpa.assemblers;

import com.acme.saferoute.platform.trip.domain.model.aggregates.Trip;
import com.acme.saferoute.platform.trip.domain.model.entities.Attendance;
import com.acme.saferoute.platform.trip.domain.model.entities.Incident;
import com.acme.saferoute.platform.trip.infrastructure.persistence.jpa.entities.AttendancePersistenceEntity;
import com.acme.saferoute.platform.trip.infrastructure.persistence.jpa.entities.IncidentPersistenceEntity;
import com.acme.saferoute.platform.trip.infrastructure.persistence.jpa.entities.TripPersistenceEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Static assembler that maps between the {@link Trip} domain aggregate (with its {@link Attendance}
 * and {@link Incident} children) and its {@link TripPersistenceEntity} relational representation.
 *
 * <p>New aggregates and children carry a {@code null} id so JPA can generate identities; existing
 * ones keep their id so the save becomes an update. Child entities receive the back-reference to the
 * owning trip persistence entity to populate their {@code trip_id} foreign key.</p>
 */
public final class TripPersistenceAssembler {

    private TripPersistenceAssembler() {
    }

    /**
     * Rebuilds a {@link Trip} domain aggregate from its persistence entity.
     *
     * @param entity the trip persistence entity (may be null)
     * @return the corresponding domain trip, or null if {@code entity} is null
     */
    public static Trip toDomainFromPersistence(TripPersistenceEntity entity) {
        if (entity == null) {
            return null;
        }

        List<Attendance> attendances = new ArrayList<>();
        for (var attendanceEntity : entity.getAttendances()) {
            attendances.add(new Attendance(
                    attendanceEntity.getId(),
                    attendanceEntity.getChildId(),
                    attendanceEntity.getBoardingState(),
                    attendanceEntity.getBoardedAt()));
        }

        List<Incident> incidents = new ArrayList<>();
        for (var incidentEntity : entity.getIncidents()) {
            incidents.add(new Incident(
                    incidentEntity.getId(),
                    incidentEntity.getDescription(),
                    incidentEntity.getReportedAt()));
        }

        return new Trip(
                entity.getId(),
                entity.getOrganizationId(),
                entity.getRouteId(),
                entity.getDriverId(),
                entity.getTripState(),
                entity.getStartTime(),
                entity.getEndTime(),
                attendances,
                incidents);
    }

    /**
     * Maps a {@link Trip} domain aggregate to a new persistence entity ready to be saved.
     *
     * @param trip the domain trip
     * @return the corresponding persistence entity, or null if {@code trip} is null
     */
    public static TripPersistenceEntity toPersistenceFromDomain(Trip trip) {
        if (trip == null) {
            return null;
        }

        var entity = new TripPersistenceEntity();
        if (trip.getId() != null) {
            entity.setId(trip.getId());
        }
        entity.setOrganizationId(trip.getOrganizationId());
        entity.setRouteId(trip.getRouteId());
        entity.setDriverId(trip.getDriverId());
        entity.setTripState(trip.getTripState());
        entity.setStartTime(trip.getStartTime());
        entity.setEndTime(trip.getEndTime());

        List<AttendancePersistenceEntity> attendanceEntities = new ArrayList<>();
        for (var attendance : trip.getAttendances()) {
            var attendanceEntity = new AttendancePersistenceEntity();
            if (attendance.getId() != null) {
                attendanceEntity.setId(attendance.getId());
            }
            attendanceEntity.setTrip(entity);
            attendanceEntity.setChildId(attendance.getChildId());
            attendanceEntity.setBoardingState(attendance.getBoardingState());
            attendanceEntity.setBoardedAt(attendance.getBoardedAt());
            attendanceEntities.add(attendanceEntity);
        }
        entity.setAttendances(attendanceEntities);

        List<IncidentPersistenceEntity> incidentEntities = new ArrayList<>();
        for (var incident : trip.getIncidents()) {
            var incidentEntity = new IncidentPersistenceEntity();
            if (incident.getId() != null) {
                incidentEntity.setId(incident.getId());
            }
            incidentEntity.setTrip(entity);
            incidentEntity.setDescription(incident.getDescription());
            incidentEntity.setReportedAt(incident.getReportedAt());
            incidentEntities.add(incidentEntity);
        }
        entity.setIncidents(incidentEntities);

        return entity;
    }
}
