package com.example;

public class PasswordValidator {
    public boolean isValid(String password) {
        return getErrorMessage(password).equals("Password is valid");
    }

    public String getErrorMessage(String password) {
        if (password == null) {
            return "Password must not be null";
        }

        if (password.length() < 8) {
            return "Password must contain at least 8 characters";
        }

        if (!containsLowercase(password)) {
            return "Password must contain at least one lowercase letter";
        }

        if (!containsUppercase(password)) {
            return "Password must contain at least one uppercase letter";
        }

        if (!containsDigit(password)) {
            return "Password must contain at least one digit";
        }

        if (!containsSpecialCharacter(password)) {
            return "Password must contain at least one special character";
        }

        return "Password is valid";
    }

    private boolean containsLowercase(String password) {
        for (char character : password.toCharArray()) {
            if (Character.isLowerCase(character)) {
                return true;
            }
        }

        return false;
    }

    private boolean containsUppercase(String password) {
        for (char character : password.toCharArray()) {
            if (Character.isUpperCase(character)) {
                return true;
            }
        }

        return false;
    }

    private boolean containsDigit(String password) {
        for (char character : password.toCharArray()) {
            if (Character.isDigit(character)) {
                return true;
            }
        }

        return false;
    }

    private boolean containsSpecialCharacter(String password) {
        for (char character : password.toCharArray()) {
            if (character == '!' || character == '@' || character == '#' || character == '$' || character == '%') {
                return true;
            }
        }

        return false;
    }
}
