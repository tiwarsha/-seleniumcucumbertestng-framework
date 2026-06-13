package com.example.automation.driver;

import com.example.automation.config.ConfigManager;
import org.openqa.selenium.WebDriver;

public final class DriverManager {
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {
    }	

    public static void startDriver() {
        if (DRIVER.get() == null) {
            WebDriver driver = DriverFactory.createDriver();
            driver.manage().timeouts().pageLoadTimeout(ConfigManager.pageLoadTimeout());
            DRIVER.set(driver);
        }
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("Driver is not initialized for this thread");
        }
        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }
}
