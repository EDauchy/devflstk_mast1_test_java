package com.example.meetingroom.exception;

public class ReservationNotFoundException extends RuntimeException {

    public ReservationNotFoundException(Long id) {
        super("Aucune réservation trouvée avec l'identifiant " + id);
    }
}
