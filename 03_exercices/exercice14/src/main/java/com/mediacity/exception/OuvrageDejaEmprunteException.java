package com.mediacity.exception;

public class OuvrageDejaEmprunteException extends RuntimeException {

    public OuvrageDejaEmprunteException(String ouvrageId) {
        super("L'ouvrage " + ouvrageId + " est déjà emprunté");
    }
}
