package com.example;

public interface CartRepository {
    boolean exists(String cartId);

    void addItem(String cartId, String productId, int quantity);

    void decreaseItem(String cartId, String productId);

    void removeItem(String cartId, String productId);

    int getQuantity(String cartId, String productId);

    boolean hasProduct(String cartId, String productId);

    void validate(String cartId);
}
