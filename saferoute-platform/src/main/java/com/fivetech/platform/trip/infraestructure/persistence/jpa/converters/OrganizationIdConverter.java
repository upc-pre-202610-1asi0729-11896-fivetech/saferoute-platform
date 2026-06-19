package com.acme.saferoute.platform.trip.infrastructure.persistence.jpa.converters;

import com.acme.saferoute.platform.trip.domain.model.valueobjects.OrganizationId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Converts {@link OrganizationId} value objects to/from their {@code Long} column representation.
 */
@Converter(autoApply = false)
public class OrganizationIdConverter implements AttributeConverter<OrganizationId, Long> {

    @Override
    public Long convertToDatabaseColumn(OrganizationId attribute) {
        return attribute == null ? null : attribute.organizationId();
    }

    @Override
    public OrganizationId convertToEntityAttribute(Long dbData) {
        return dbData == null ? null : new OrganizationId(dbData);
    }
}
