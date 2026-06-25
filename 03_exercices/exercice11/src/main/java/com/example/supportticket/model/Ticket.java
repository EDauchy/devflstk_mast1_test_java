package com.example.supportticket.model;

public record Ticket(Long id, String title, Priority priority, TicketStatus status) {
}
