package com.example.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CartPage extends BasePage {
    private static final By CART_ITEM_NAMES = By.cssSelector("td.product-name a");
    private static final By QUANTITY_INPUT = By.cssSelector("input.qty");
    private static final By UPDATE_CART_BUTTON = By.cssSelector("button[name='update_cart']");
    private static final By CHECKOUT_BUTTON = By.cssSelector("a.checkout-button");

    public boolean containsProduct(String product) {
        waitUntil().until(ExpectedConditions.visibilityOfElementLocated(CART_ITEM_NAMES));
        return driver().findElements(CART_ITEM_NAMES).stream()
                .anyMatch(name -> name.getText().equalsIgnoreCase(product));
    }

    public void setQuantity(int quantity) {
        type(QUANTITY_INPUT, String.valueOf(quantity));
        click(UPDATE_CART_BUTTON);
        waitForOverlayToDisappear();
        waitUntil().until(ExpectedConditions.attributeToBe(QUANTITY_INPUT, "value", String.valueOf(quantity)));
    }

    public void proceedToCheckout() {
        waitForOverlayToDisappear();
        click(CHECKOUT_BUTTON);
    }
}
