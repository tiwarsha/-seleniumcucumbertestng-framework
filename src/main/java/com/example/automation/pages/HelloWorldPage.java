package com.example.automation.pages;

import com.example.automation.config.ConfigManager;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HelloWorldPage extends BasePage {
    private static final By GREETING = By.cssSelector("[data-testid='greeting']");
    private static final By REFRESH_BUTTON = By.cssSelector("[data-testid='refresh']");
    private static final By TITLE = By.cssSelector("[data-testid='app-title']");

    public void open() {
        driver().get(ConfigManager.baseUrl());
        waitUntil().until(ExpectedConditions.visibilityOfElementLocated(TITLE));
    }

    public String heading() {
        return waitUntil().until(ExpectedConditions.visibilityOfElementLocated(TITLE)).getText();
    }

    public String greeting(String expected) {
        waitUntil().until(ExpectedConditions.textToBe(GREETING, expected));
        return driver().findElement(GREETING).getText();
    }

    public void refreshGreeting() {
        waitUntil().until(ExpectedConditions.elementToBeClickable(REFRESH_BUTTON)).click();
    }
}
