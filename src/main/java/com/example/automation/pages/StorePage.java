package com.example.automation.pages;

import com.example.automation.config.ConfigManager;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class StorePage extends BasePage {
    private static final By STORE_MENU = By.cssSelector("li.menu-item a[href$='/store/']");
    private static final By SEARCH_FIELD = By.cssSelector("form.woocommerce-product-search input.search-field");
    private static final By SEARCH_BUTTON = By.cssSelector("form.woocommerce-product-search button[type='submit']");
    private static final By PRODUCT_TITLES = By.cssSelector("h2.woocommerce-loop-product__title");
    private static final By VIEW_CART_LINK = By.cssSelector("a.added_to_cart");

    public void openStore() {
        driver().get(ConfigManager.baseUrl());
        click(STORE_MENU);
        waitUntil().until(ExpectedConditions.visibilityOfElementLocated(SEARCH_FIELD));
    }

    public void searchProduct(String product) {
        type(SEARCH_FIELD, product);
        click(SEARCH_BUTTON);
        waitUntil().until(ExpectedConditions.visibilityOfElementLocated(PRODUCT_TITLES));
    }

    public boolean isProductListed(String product) {
        return driver().findElements(PRODUCT_TITLES).stream()
                .anyMatch(title -> title.getText().equalsIgnoreCase(product));
    }

    public void addProductToCart(String product) {
        click(addToCartButton(product));
        waitUntil().until(ExpectedConditions.visibilityOfElementLocated(VIEW_CART_LINK));
    }

    public void openCart() {
        click(VIEW_CART_LINK);
    }

    private By addToCartButton(String product) {
        return By.cssSelector(String.format("a.add_to_cart_button[aria-label*=\"%s\"]", product));
    }
}
