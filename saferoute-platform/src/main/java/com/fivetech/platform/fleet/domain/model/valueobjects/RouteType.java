package com.fivetech.platform.fleet.domain.model.valueobjects;

/**
 * Enumeration representing the direction of a {@code Route}.
 *
 * <p>An outbound route picks children up at home stops and delivers them to school; an inbound
 * route is the return journey. Matches the {@code type} field used by the SafeRoute clients.</p>
 */
public enum RouteType {
    /**
     * Home-to-school direction. Default when unspecified.
     */
    OUTBOUND,

    /**
     * School-to-home (return) direction.
     */
    INBOUND;

    /**
     * Parses a textual route type, applying the OUTBOUND default for null/blank input.
     *
     * @param value the textual route type (case-insensitive), or null
     * @return the parsed {@link RouteType}
     * @throws IllegalArgumentException if {@code value} is non-blank but not a known type
     */
    public static RouteType fromNullable(String value) {
        if (value == null || value.isBlank()) {
            return OUTBOUND;
        }
        try {
            return RouteType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                    "Unknown route type '%s'. Allowed values: OUTBOUND, INBOUND".formatted(value));
        }
    }
}
