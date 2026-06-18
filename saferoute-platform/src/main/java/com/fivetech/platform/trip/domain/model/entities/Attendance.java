package com.acme.saferoute.platform.trip.domain.model.entities;

import com.acme.saferoute.platform.trip.domain.model.valueobjects.BoardingState;
import com.acme.saferoute.platform.trip.domain.model.valueobjects.ChildId;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Objects;

/**
 * Attendance domain entity.
 *
 * <p>Tracks the boarding status of a single child during a trip (US-11). It is a child entity of the
 * {@code Trip} aggregate: its lifecycle and consistency are governed by the trip. A child is
 * recorded as {@link BoardingState#PENDING} by default and updated as the trip progresses; the
 * {@code boardedAt} timestamp is captured the moment the child boards.</p>
 */
@Getter
public class Attendance {

    /**
     * Persistence identity of the attendance record. Assigned by the infrastructure layer.
     */
    @Setter
    private Long id;

    /**
     * Identifier of the child this attendance refers to.
     */
    private final ChildId childId;

    /**
     * Current boarding state of the child.
     */
    private BoardingState boardingState;

    /**
     * Timestamp at which the child boarded, or null if not boarded yet.
     */
    private Instant boardedAt;

    /**
     * Domain constructor for a brand-new attendance, defaulting to PENDING.
     *
     * @param childId identifier of the child
     */
    public Attendance(ChildId childId) {
        this.childId = Objects.requireNonNull(childId, "childId must not be null");
        this.boardingState = BoardingState.PENDING;
    }

    /**
     * Full reconstruction constructor used by the persistence assembler.
     *
     * @param id            persistence identity
     * @param childId       identifier of the child
     * @param boardingState current boarding state
     * @param boardedAt     boarding timestamp, or null
     */
    public Attendance(Long id, ChildId childId, BoardingState boardingState, Instant boardedAt) {
        this.id = id;
        this.childId = childId;
        this.boardingState = boardingState;
        this.boardedAt = boardedAt;
    }

    /**
     * Applies a new boarding state. When transitioning to {@link BoardingState#BOARDED}, the
     * boarding timestamp is captured.
     *
     * @param newState the new boarding state
     */
    public void changeState(BoardingState newState) {
        this.boardingState = Objects.requireNonNull(newState, "boardingState must not be null");
        if (newState == BoardingState.BOARDED) {
            this.boardedAt = Instant.now();
        }
    }

    /**
     * Indicates whether the child is currently on board the vehicle.
     *
     * @return true if the boarding state is BOARDED
     */
    public boolean isOnBoard() {
        return this.boardingState == BoardingState.BOARDED;
    }
}
