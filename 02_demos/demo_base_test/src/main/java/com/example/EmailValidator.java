package com.example;

public class EmailValidator {
    public boolean isValid(String email) {
        if (email == null) {
            throw new IllegalArgumentException("Email must not be null");
        }

        if (email.isBlank()) {
            return false;
        }

        if (!email.contains("@")) {
            return false;
        }

        if (!email.contains(".")) {
            return false;
        }

        if (email.startsWith("@")) {
            return false;
        }

        if (email.endsWith("@")) {
            return false;
        }

        return true;
    }
}
