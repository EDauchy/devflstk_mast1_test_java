package com.example.bankapi.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferRequest(
        @NotBlank(message = "Le compte émetteur est obligatoire")
        String fromAccount,
        @NotBlank(message = "Le compte destinataire est obligatoire")
        String toAccount,
        @NotNull(message = "Le montant est obligatoire")
        @DecimalMin(value = "0.01", message = "Le montant doit être strictement positif")
        BigDecimal amount
) {
}
