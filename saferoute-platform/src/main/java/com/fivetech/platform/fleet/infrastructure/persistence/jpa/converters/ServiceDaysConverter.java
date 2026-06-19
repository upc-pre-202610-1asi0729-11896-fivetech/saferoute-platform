package com.fivetech.platform.fleet.infrastructure.persistence.jpa.converters;

import com.fivetech.platform.fleet.domain.model.valueobjects.ServiceDays;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;

/**
 * Converts the {@link ServiceDays} value object to/from a comma-separated {@code String} column.
 *
 * <p>Storing the weekday codes as a single delimited column keeps the {@code routes} table
 * self-contained while the domain continues to work with a typed list of service days.</p>
 */
@Converter(autoApply = false)
public class ServiceDaysConverter implements AttributeConverter<ServiceDays, String> {

    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(ServiceDays attribute) {
        return attribute == null ? null : String.join(DELIMITER, attribute.days());
    }

    @Override
    public ServiceDays convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return null;
        }
        List<String> days = Arrays.stream(dbData.split(DELIMITER))
                .map(String::trim)
                .filter(day -> !day.isBlank())
                .toList();
        return new ServiceDays(days);
    }
}
