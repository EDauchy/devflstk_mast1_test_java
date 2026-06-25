package com.example.supportticket.exception;

public class TicketNotFoundException extends RuntimeException {

    public TicketNotFoundException(Long id) {
        super("Aucun ticket trouvé avec l'identifiant " + id);
    }
}
