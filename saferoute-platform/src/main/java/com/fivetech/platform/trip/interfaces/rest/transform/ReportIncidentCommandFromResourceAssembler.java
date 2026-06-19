package com.acme.saferoute.platform.trip.interfaces.rest.transform;

import com.acme.saferoute.platform.trip.domain.model.commands.ReportIncidentCommand;
import com.acme.saferoute.platform.trip.interfaces.rest.resources.ReportIncidentResource;

/**
 * Assembler converting a {@link ReportIncidentResource} (plus the trip id from the URL path) into a
 * {@link ReportIncidentCommand}.
 */
public final class ReportIncidentCommandFromResourceAssembler {

    private ReportIncidentCommandFromResourceAssembler() {
    }

    /**
     * Builds the command from the trip id and the inbound resource.
     *
     * @param tripId   identifier of the trip
     * @param resource the report-incident resource
     * @return the corresponding command
     */
    public static ReportIncidentCommand toCommandFromResource(Long tripId, ReportIncidentResource resource) {
        return new ReportIncidentCommand(tripId, resource.description());
    }
}
