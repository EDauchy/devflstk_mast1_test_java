package com.example.supportticket.model;

public record TicketResponse(Long id, String title, Priority priority, TicketStatus status) {

    public static TicketResponse from(Ticket ticket) {
        return new TicketResponse(ticket.id(), ticket.title(), ticket.priority(), ticket.status());
    }
}
