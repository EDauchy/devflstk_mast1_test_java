package com.example.supportticket.exception;

import com.example.supportticket.model.TicketStatus;

public class InvalidStatusTransitionException extends RuntimeException {

    public InvalidStatusTransitionException(TicketStatus current, TicketStatus requested) {
        super("Transition de statut interdite de " + current + " vers " + requested);
    }
}
