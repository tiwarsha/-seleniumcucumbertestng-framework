package com.example.automation.pages;

import com.example.automation.config.ConfigManager;
import com.example.automation.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class BasePage {
    protected WebDriver driver() {
        return DriverManager.getDriver();
    }

    protected WebDriverWait waitUntil() {
        return new WebDriverWait(driver(), ConfigManager.explicitWait());
    }

    protected WebElement visible(By locator) {
        return waitUntil().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement clickable(By locator) {
        return waitUntil().until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(locator)));
    }

    protected void click(By locator) {
        waitUntil().until(driver -> {
            try {
                clickable(locator).click();
                return true;
            } catch (StaleElementReferenceException | ElementClickInterceptedException e) {
                return false;
            }
        });
    }

    protected void waitForOverlayToDisappear() {
        waitUntil().until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".blockUI.blockOverlay")));
    }

    protected void type(By locator, String text) {
        WebElement field = visible(locator);
        field.clear();
        field.sendKeys(text);
    }

    protected void selectByValue(By locator, String value) {
        WebElement select = waitUntil().until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) driver()).executeScript(
                "arguments[0].value = arguments[1];"
                        + "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                select, value);
    }

    protected boolean isDisplayed(By locator) {
        return !driver().findElements(locator).isEmpty() && driver().findElement(locator).isDisplayed();
    }
}
