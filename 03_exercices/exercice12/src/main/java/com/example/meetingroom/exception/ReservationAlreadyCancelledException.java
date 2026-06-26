package com.example.meetingroom.exception;

public class ReservationAlreadyCancelledException extends RuntimeException {

    public ReservationAlreadyCancelledException(Long id) {
        super("La réservation " + id + " est déjà annulée");
    }
}
