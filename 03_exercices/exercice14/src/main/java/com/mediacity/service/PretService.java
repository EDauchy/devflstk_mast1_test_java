package com.mediacity.service;

import com.mediacity.exception.OuvrageDejaEmprunteException;
import com.mediacity.model.Pret;
import com.mediacity.model.StatutPret;
import com.mediacity.repository.PretRepository;

import java.time.LocalDate;
import java.util.UUID;

public class PretService {

    static final int DUREE_PRET_JOURS = 21;

    private final PretRepository pretRepository;

    public PretService(PretRepository pretRepository) {
        this.pretRepository = pretRepository;
    }

    public Pret creerPret(String adherentId, String ouvrageId, LocalDate dateEmprunt) {
        if (pretRepository.findPretEnCoursParOuvrage(ouvrageId).isPresent()) {
            throw new OuvrageDejaEmprunteException(ouvrageId);
        }

        LocalDate dateRetourPrevue = dateEmprunt.plusDays(DUREE_PRET_JOURS);
        Pret pret = new Pret(
                UUID.randomUUID().toString(),
                adherentId,
                ouvrageId,
                dateEmprunt,
                dateRetourPrevue,
                null,
                StatutPret.EN_COURS
        );

        return pretRepository.save(pret);
    }
}
