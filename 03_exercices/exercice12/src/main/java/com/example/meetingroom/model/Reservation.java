package com.example.meetingroom.model;

import java.time.LocalDateTime;

public record Reservation(
        Long id,
        Long roomId,
        String reservedBy,
        LocalDateTime startTime,
        LocalDateTime endTime,
        ReservationStatus status
) {
}
