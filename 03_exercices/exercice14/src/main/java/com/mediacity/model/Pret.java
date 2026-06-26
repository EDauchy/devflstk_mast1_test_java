package com.mediacity.model;

import java.time.LocalDate;

public record Pret(
        String id,
        String adherentId,
        String ouvrageId,
        LocalDate dateEmprunt,
        LocalDate dateRetourPrevue,
        LocalDate dateRetourEffective,
        StatutPret statut
) {
}
