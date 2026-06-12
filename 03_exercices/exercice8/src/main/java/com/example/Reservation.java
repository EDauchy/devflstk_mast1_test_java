package com.example;

import java.time.LocalDateTime;

public class Reservation {
    private final String userEmail;
    private final String roomCode;
    private final int participants;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public Reservation(String userEmail, String roomCode, int participants,
                       LocalDateTime startDate, LocalDateTime endDate) {
        this.userEmail = userEmail;
        this.roomCode = roomCode;
        this.participants = participants;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public int getParticipants() {
        return participants;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }
}
