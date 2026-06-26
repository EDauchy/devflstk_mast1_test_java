package com.mediacity.repository;

import com.mediacity.model.Pret;

import java.util.Optional;

public interface PretRepository {

    Pret save(Pret pret);

    Optional<Pret> findPretEnCoursParOuvrage(String ouvrageId);
}
