package com.example.meetingroom.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateRoomRequest(
        @NotBlank(message = "Le nom est obligatoire")
        String name,
        @Min(value = 1, message = "La capacité doit être supérieure ou égale à 1")
        int capacity
) {
}
