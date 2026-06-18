package com.acme.saferoute.platform.trip.domain.model.commands;

/**
 * Command expressing the intent to update a child's boarding status during a trip (US-11).
 *
 * @param tripId        identifier of the trip
 * @param childId       identifier of the child whose boarding state changes
 * @param boardingState the new boarding state code (PENDING, BOARDED, ABSENT, DROPPED_OFF)
 */
public record UpdateBoardingStatusCommand(
        Long tripId,
        Long childId,
        String boardingState) {
}
