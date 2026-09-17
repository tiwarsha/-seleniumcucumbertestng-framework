package com.example.automation.stepdefinitions;

import static org.testng.Assert.assertTrue;

import com.example.automation.model.BillingDetails;
import com.example.automation.pages.CartPage;
import com.example.automation.pages.CheckoutPage;
import com.example.automation.pages.StorePage;
import com.example.automation.utils.Log;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;

public class EndToEndPurchaseSteps {
    private static final Logger LOG = Log.get(EndToEndPurchaseSteps.class);
    private static final BillingDetails BILLING_DETAILS = new BillingDetails(
            "Sharad",
            "Tiwari",
            "IN",
            "12 Automation Street",
            "Pune",
            "MH",
            "411001",
            "9876543210",
            "sharad.qa@example.com");

    private final StorePage storePage = new StorePage();
    private final CartPage cartPage = new CartPage();
    private final CheckoutPage checkoutPage = new CheckoutPage();

    @Given("I am on the store page")
    public void iAmOnTheStorePage() {
        storePage.openStore();
    }

    @When("I search for {string}")
    public void iSearchFor(String searchTerm) {
        storePage.searchProduct(searchTerm);
    }

    @Then("the product {string} is listed in the search results")
    public void theProductIsListedInTheSearchResults(String product) {
        assertTrue(storePage.isProductListed(product), "Product not listed in search results: " + product);
    }

    @When("I add the product {string} to the cart")
    public void iAddTheProductToTheCart(String product) {
        storePage.addProductToCart(product);
    }

    @When("I open the cart")
    public void iOpenTheCart() {
        storePage.openCart();
    }

    @Then("the cart contains the product {string}")
    public void theCartContainsTheProduct(String product) {
        assertTrue(cartPage.containsProduct(product), "Product missing from cart: " + product);
    }

    @When("I set the cart quantity to {int}")
    public void iSetTheCartQuantityTo(int quantity) {
        cartPage.setQuantity(quantity);
    }

    @When("I proceed to checkout")
    public void iProceedToCheckout() {
        cartPage.proceedToCheckout();
    }

    @And("I fill in the billing details")
    public void iFillInTheBillingDetails() {
        checkoutPage.fillBillingDetails(BILLING_DETAILS);
    }

    @When("I place the order with direct bank transfer")
    public void iPlaceTheOrderWithDirectBankTransfer() {
        checkoutPage.selectDirectBankTransfer();
        checkoutPage.placeOrder();
    }

    @Then("the order is placed successfully")
    public void theOrderIsPlacedSuccessfully() {
        String message = checkoutPage.orderConfirmationMessage();
        LOG.info("Order confirmation: {}", message);
        assertTrue(message.toLowerCase().contains("thank you"), "Unexpected confirmation message: " + message);
    }
}
