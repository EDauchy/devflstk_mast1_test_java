package com.example.bankapi.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateAccountRequest(
        @NotBlank(message = "Le numéro de compte est obligatoire")
        String number,
        @NotBlank(message = "Le titulaire est obligatoire")
        String holder
) {
}
