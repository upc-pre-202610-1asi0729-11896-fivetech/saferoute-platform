package com.fivetech.platform.fleet.infrastructure.persistence.jpa.converters;

import com.fivetech.platform.fleet.domain.model.valueobjects.VehicleId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Converts {@link VehicleId} value objects to/from their {@code Long} column representation.
 */
@Converter(autoApply = false)
public class VehicleIdConverter implements AttributeConverter<VehicleId, Long> {

    @Override
    public Long convertToDatabaseColumn(VehicleId attribute) {
        return attribute == null ? null : attribute.vehicleId();
    }

    @Override
    public VehicleId convertToEntityAttribute(Long dbData) {
        return dbData == null ? null : new VehicleId(dbData);
    }
}
