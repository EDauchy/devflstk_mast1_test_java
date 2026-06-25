package com.example.supportticket.model;

import jakarta.validation.constraints.NotNull;

public record UpdateTicketStatusRequest(
        @NotNull(message = "Le statut est obligatoire")
        TicketStatus status
) {
}
