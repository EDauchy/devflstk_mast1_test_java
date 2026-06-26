package com.example.meetingroom.exception;

public class RoomNotFoundException extends RuntimeException {

    public RoomNotFoundException(Long id) {
        super("Aucune salle trouvée avec l'identifiant " + id);
    }
}
