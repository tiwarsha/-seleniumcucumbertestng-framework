package com.example.automation.pages;

import com.example.automation.model.BillingDetails;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CheckoutPage extends BasePage {
    private static final By FIRST_NAME = By.id("billing_first_name");
    private static final By LAST_NAME = By.id("billing_last_name");
    private static final By COUNTRY = By.id("billing_country");
    private static final By ADDRESS_LINE_1 = By.id("billing_address_1");
    private static final By CITY = By.id("billing_city");
    private static final By STATE = By.id("billing_state");
    private static final By POSTCODE = By.id("billing_postcode");
    private static final By PHONE = By.id("billing_phone");
    private static final By EMAIL = By.id("billing_email");
    private static final By DIRECT_BANK_TRANSFER = By.id("payment_method_bacs");
    private static final By PLACE_ORDER = By.id("place_order");
    private static final By ORDER_CONFIRMATION = By.cssSelector("p.woocommerce-notice--success");

    public void fillBillingDetails(BillingDetails billingDetails) {
        type(FIRST_NAME, billingDetails.firstName());
        type(LAST_NAME, billingDetails.lastName());
        selectByValue(COUNTRY, billingDetails.countryCode());
        type(ADDRESS_LINE_1, billingDetails.addressLine1());
        type(CITY, billingDetails.city());
        selectByValue(STATE, billingDetails.stateCode());
        type(POSTCODE, billingDetails.postcode());
        type(PHONE, billingDetails.phone());
        type(EMAIL, billingDetails.email());
        waitForOverlayToDisappear();
    }

    public void selectDirectBankTransfer() {
        clickable(DIRECT_BANK_TRANSFER).click();
    }

    public void placeOrder() {
        click(PLACE_ORDER);
        waitUntil().until(ExpectedConditions.visibilityOfElementLocated(ORDER_CONFIRMATION));
    }

    public String orderConfirmationMessage() {
        return visible(ORDER_CONFIRMATION).getText();
    }
}
