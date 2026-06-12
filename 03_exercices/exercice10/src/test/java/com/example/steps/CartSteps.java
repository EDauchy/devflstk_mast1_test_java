package com.example.steps;

import com.example.CartRepository;
import com.example.CartService;
import com.example.Product;
import com.example.ProductRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CartSteps {
    private CartRepository cartRepository;
    private ProductRepository productRepository;
    private CartService cartService;
    private String confirmationMessage;
    private String orderConfirmation;
    private Exception cartError;

    @Given("cart {string} exists")
    public void cartExists(String cartId) {
        // Given
        initServices();
        when(cartRepository.exists(cartId)).thenReturn(true);
    }

    @Given("cart {string} does not exist")
    public void cartDoesNotExist(String cartId) {
        // Given
        initServices();
        when(cartRepository.exists(cartId)).thenReturn(false);
    }

    @Given("product {string} exists")
    public void productExists(String productId) {
        // Given
        if (cartRepository == null) {
            initServices();
        }
        when(productRepository.findById(productId))
                .thenReturn(Optional.of(new Product(productId, "Product " + productId, "General", 10.0)));
    }

    @Given("product {string} is not in cart {string}")
    public void productIsNotInCart(String productId, String cartId) {
        // Given
        when(cartRepository.hasProduct(cartId, productId)).thenReturn(false);
    }

    @Given("product {string} is already in cart {string} with quantity {int}")
    public void productIsAlreadyInCartWithQuantity(String productId, String cartId, int quantity) {
        // Given
        when(cartRepository.hasProduct(cartId, productId)).thenReturn(true);
        when(cartRepository.getQuantity(cartId, productId)).thenReturn(quantity);
    }

    @When("the user adds product {string} to cart {string}")
    public void theUserAddsProductToCart(String productId, String cartId) {
        // When
        confirmationMessage = cartService.addToCart(cartId, productId);
    }

    @When("the user tries to add product {string} to cart {string}")
    public void theUserTriesToAddProductToCart(String productId, String cartId) {
        // When
        cartError = assertThrows(
                IllegalArgumentException.class,
                () -> cartService.addToCart(cartId, productId)
        );
    }

    @When("the user removes product {string} from cart {string}")
    public void theUserRemovesProductFromCart(String productId, String cartId) {
        // When
        cartService.decreaseQuantity(cartId, productId);
    }

    @When("the user tries to remove product {string} from cart {string}")
    public void theUserTriesToRemoveProductFromCart(String productId, String cartId) {
        // When
        cartError = assertThrows(
                IllegalArgumentException.class,
                () -> cartService.decreaseQuantity(cartId, productId)
        );
    }

    @When("the user validates order for cart {string}")
    public void theUserValidatesOrderForCart(String cartId) {
        // When
        orderConfirmation = cartService.validateOrder(cartId);
    }

    @When("the user tries to validate order for cart {string}")
    public void theUserTriesToValidateOrderForCart(String cartId) {
        // When
        cartError = assertThrows(
                IllegalArgumentException.class,
                () -> cartService.validateOrder(cartId)
        );
    }

    @Then("the add to cart confirmation should be {string}")
    public void theAddToCartConfirmationShouldBe(String expectedMessage) {
        // Then
        assertEquals(expectedMessage, confirmationMessage);
    }

    @Then("cart {string} should contain product {string} with quantity {int}")
    public void cartShouldContainProductWithQuantity(String cartId, String productId, int quantity) {
        // Then
        verify(cartRepository).addItem(cartId, productId, quantity);
    }

    @Then("cart {string} should have product {string} quantity decreased")
    public void cartShouldHaveProductQuantityDecreased(String cartId, String productId) {
        // Then
        verify(cartRepository).decreaseItem(cartId, productId);
    }

    @Then("product {string} should be removed from cart {string}")
    public void productShouldBeRemovedFromCart(String productId, String cartId) {
        // Then
        verify(cartRepository).removeItem(cartId, productId);
    }

    @Then("the order confirmation should be {string}")
    public void theOrderConfirmationShouldBe(String expectedMessage) {
        // Then
        assertEquals(expectedMessage, orderConfirmation);
    }

    @Then("cart {string} should be validated")
    public void cartShouldBeValidated(String cartId) {
        // Then
        verify(cartRepository).validate(cartId);
    }

    @Then("a cart error should be returned with message {string}")
    public void aCartErrorShouldBeReturnedWithMessage(String expectedMessage) {
        // Then
        assertEquals(expectedMessage, cartError.getMessage());
    }

    private void initServices() {
        cartRepository = mock(CartRepository.class);
        productRepository = mock(ProductRepository.class);
        cartService = new CartService(cartRepository, productRepository);
    }
}
