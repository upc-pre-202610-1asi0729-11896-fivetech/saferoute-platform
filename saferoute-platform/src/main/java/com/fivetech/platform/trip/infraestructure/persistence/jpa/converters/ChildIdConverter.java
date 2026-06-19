package com.acme.saferoute.platform.trip.infrastructure.persistence.jpa.converters;

import com.acme.saferoute.platform.trip.domain.model.valueobjects.ChildId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Converts {@link ChildId} value objects to/from their {@code Long} column representation,
 * used by the attendance child entity.
 */
@Converter(autoApply = false)
public class ChildIdConverter implements AttributeConverter<ChildId, Long> {

    @Override
    public Long convertToDatabaseColumn(ChildId attribute) {
        return attribute == null ? null : attribute.childId();
    }

    @Override
    public ChildId convertToEntityAttribute(Long dbData) {
        return dbData == null ? null : new ChildId(dbData);
    }
}
