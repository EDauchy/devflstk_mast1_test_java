package com.example;

public class LoginResult {
    private final boolean success;
    private final String message;
    private final boolean redirectedToHome;

    public LoginResult(boolean success, String message, boolean redirectedToHome) {
        this.success = success;
        this.message = message;
        this.redirectedToHome = redirectedToHome;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public boolean isRedirectedToHome() {
        return redirectedToHome;
    }
}
