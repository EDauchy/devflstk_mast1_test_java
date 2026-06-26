package com.example.meetingroom.dto;

import com.example.meetingroom.model.Reservation;
import com.example.meetingroom.model.ReservationStatus;

import java.time.LocalDateTime;

public record ReservationResponse(
        Long id,
        Long roomId,
        String reservedBy,
        LocalDateTime startTime,
        LocalDateTime endTime,
        ReservationStatus status
) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.id(),
                reservation.roomId(),
                reservation.reservedBy(),
                reservation.startTime(),
                reservation.endTime(),
                reservation.status()
        );
    }
}
