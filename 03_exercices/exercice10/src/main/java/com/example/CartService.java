package com.example;

public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public String addToCart(String cartId, String productId) {
        if (!cartRepository.exists(cartId)) {
            throw new IllegalArgumentException("Cart does not exist");
        }

        productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (cartRepository.hasProduct(cartId, productId)) {
            int currentQuantity = cartRepository.getQuantity(cartId, productId);
            cartRepository.addItem(cartId, productId, currentQuantity + 1);
        } else {
            cartRepository.addItem(cartId, productId, 1);
        }

        return "Product " + productId + " added to cart";
    }

    public void decreaseQuantity(String cartId, String productId) {
        if (!cartRepository.exists(cartId)) {
            throw new IllegalArgumentException("Cart does not exist");
        }

        if (!cartRepository.hasProduct(cartId, productId)) {
            throw new IllegalArgumentException("Product not in cart");
        }

        int quantity = cartRepository.getQuantity(cartId, productId);
        if (quantity > 1) {
            cartRepository.decreaseItem(cartId, productId);
        } else {
            cartRepository.removeItem(cartId, productId);
        }
    }

    public String validateOrder(String cartId) {
        if (!cartRepository.exists(cartId)) {
            throw new IllegalArgumentException("Cart does not exist");
        }

        cartRepository.validate(cartId);
        return "Order validated successfully for cart " + cartId;
    }
}
