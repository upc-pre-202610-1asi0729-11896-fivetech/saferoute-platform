package com.fivetech.platform.fleet.infrastructure.persistence.jpa.converters;

import com.fivetech.platform.fleet.domain.model.valueobjects.DepartureTime;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.LocalTime;

/**
 * Converts the {@link DepartureTime} value object to/from a {@link LocalTime} column
 * (mapped to a SQL {@code TIME} type), keeping the time-of-day invariant in the domain while
 * storing a native temporal value in the database.
 */
@Converter(autoApply = false)
public class DepartureTimeConverter implements AttributeConverter<DepartureTime, LocalTime> {

    @Override
    public LocalTime convertToDatabaseColumn(DepartureTime attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public DepartureTime convertToEntityAttribute(LocalTime dbData) {
        return dbData == null ? null : new DepartureTime(dbData);
    }
}
