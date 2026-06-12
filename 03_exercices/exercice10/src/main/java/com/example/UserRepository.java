package com.example;

public interface UserRepository {
    boolean existsByUsername(String username);

    void save(String email, String username, String password);

    boolean authenticate(String username, String password);
}
