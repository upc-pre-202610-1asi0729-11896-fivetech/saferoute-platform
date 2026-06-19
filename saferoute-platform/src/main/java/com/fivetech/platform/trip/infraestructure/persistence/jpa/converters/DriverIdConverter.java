package com.acme.saferoute.platform.trip.infrastructure.persistence.jpa.converters;

import com.acme.saferoute.platform.trip.domain.model.valueobjects.DriverId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Converts {@link DriverId} value objects to/from their {@code Long} column representation.
 */
@Converter(autoApply = false)
public class DriverIdConverter implements AttributeConverter<DriverId, Long> {

    @Override
    public Long convertToDatabaseColumn(DriverId attribute) {
        return attribute == null ? null : attribute.driverId();
    }

    @Override
    public DriverId convertToEntityAttribute(Long dbData) {
        return dbData == null ? null : new DriverId(dbData);
    }
}
