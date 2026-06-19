package com.acme.saferoute.platform.trip.infrastructure.persistence.jpa.converters;

import com.acme.saferoute.platform.trip.domain.model.valueobjects.RouteId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Converts {@link RouteId} value objects to/from their {@code Long} column representation.
 */
@Converter(autoApply = false)
public class RouteIdConverter implements AttributeConverter<RouteId, Long> {

    @Override
    public Long convertToDatabaseColumn(RouteId attribute) {
        return attribute == null ? null : attribute.routeId();
    }

    @Override
    public RouteId convertToEntityAttribute(Long dbData) {
        return dbData == null ? null : new RouteId(dbData);
    }
}
