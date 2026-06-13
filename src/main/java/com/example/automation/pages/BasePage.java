package com.example.automation.pages;

import com.example.automation.config.ConfigManager;
import com.example.automation.driver.DriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class BasePage {
    protected WebDriver driver() {
        return DriverManager.getDriver();
    }

    protected WebDriverWait waitUntil() {
        return new WebDriverWait(driver(), ConfigManager.explicitWait());
    }
}
