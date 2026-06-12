package com.example;

public interface CustomerRepository {
    CustomerProfile getProfileByEmail(String email);
}
