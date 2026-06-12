package com.example;

public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LoginResult login(String username, String password) {
        if (!userRepository.authenticate(username, password)) {
            return new LoginResult(false, "Invalid username or password", false);
        }

        return new LoginResult(true, "Welcome " + username, true);
    }
}
