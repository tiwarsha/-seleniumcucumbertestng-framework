package com.example.automation.driver;

import com.example.automation.config.ConfigManager;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public final class DriverFactory {
    private DriverFactory() {
    }

    public static WebDriver createDriver() {
        Capabilities capabilities = capabilitiesFor(ConfigManager.browser());
        if (ConfigManager.remote()) {
            return createRemoteDriver(capabilities);
        }
        return createLocalDriver(ConfigManager.browser(), capabilities);
    }

    private static WebDriver createLocalDriver(String browser, Capabilities capabilities) {
        return switch (browser) {
            case "firefox" -> new FirefoxDriver((FirefoxOptions) capabilities);
            case "edge" -> new EdgeDriver((EdgeOptions) capabilities);
            case "chrome" -> new ChromeDriver((ChromeOptions) capabilities);
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        };
    }

    private static WebDriver createRemoteDriver(Capabilities capabilities) {
        if (ConfigManager.gridUrl().isBlank()) {
            throw new IllegalArgumentException("gridUrl is required when remote=true");
        }
        try {
            return new RemoteWebDriver(URI.create(ConfigManager.gridUrl()).toURL(), capabilities);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid gridUrl: " + ConfigManager.gridUrl(), e);
        }
    }

    private static Capabilities capabilitiesFor(String browser) {
        return switch (browser) {
            case "firefox" -> firefoxOptions();
            case "edge" -> edgeOptions();
            case "chrome" -> chromeOptions();
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        };
    }

    private static ChromeOptions chromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized", "--disable-notifications");
        if (ConfigManager.headless()) {
            options.addArguments("--headless=new", "--window-size=1920,1080");
        }
        addCiMetadata(options);
        return options;
    }

    private static FirefoxOptions firefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        if (ConfigManager.headless()) {
            options.addArguments("-headless");
        }
        addCiMetadata(options);
        return options;
    }

    private static EdgeOptions edgeOptions() {
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--start-maximized", "--disable-notifications");
        if (ConfigManager.headless()) {
            options.addArguments("--headless=new", "--window-size=1920,1080");
        }
        addCiMetadata(options);
        return options;
    }

    private static void addCiMetadata(MutableCapabilities capabilities) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("env", ConfigManager.env());
        metadata.put("browser", ConfigManager.browser());
        capabilities.setCapability("automation:options", metadata);
    }
}
