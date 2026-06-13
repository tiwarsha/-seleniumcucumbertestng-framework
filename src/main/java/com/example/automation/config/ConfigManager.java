package com.example.automation.config;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;

public final class ConfigManager {
    private static final String DEFAULT_ENV = "qa";
    private static final Properties PROPERTIES = loadProperties();

    private ConfigManager() {
    }

    
    public static void main(String[] args) {
		String s = env();
		
		System.out.println(s);
	}
    public static String env() {
        return read("env", DEFAULT_ENV).toLowerCase(Locale.ROOT);
    }

    public static String browser() {
        return read("browser", "chrome").toLowerCase(Locale.ROOT);
    }

    public static boolean headless() {
        return Boolean.parseBoolean(read("headless", "false"));
    }

    public static boolean remote() {
        return Boolean.parseBoolean(read("remote", "false"));
    }

    public static String gridUrl() {
        return read("gridUrl", "");
    }

    public static String baseUrl() {
        String envSpecificKey = "baseUrl." + env();
        return read(envSpecificKey, read("baseUrl", "https://example.com"));
    }

    public static Duration explicitWait() {
        return Duration.ofSeconds(Long.parseLong(read("explicitWaitSeconds", "10")));
    }

    public static Duration pageLoadTimeout() {
        return Duration.ofSeconds(Long.parseLong(read("pageLoadTimeoutSeconds", "30")));
    }

    private static String read(String key, String defaultValue) {
        String systemValue = System.getProperty(key);
        if (hasText(systemValue)) {
            return systemValue;
        }

        String envValue = System.getenv(toEnvKey(key));
        if (hasText(envValue)) {
            return envValue;
        }

        return PROPERTIES.getProperty(key, defaultValue);
    }

    private static String toEnvKey(String key) {
        return key.replaceAll("([a-z])([A-Z])", "$1_$2")
                .replace('.', '_')
                .toUpperCase(Locale.ROOT);
    }

    private static boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream stream = ConfigManager.class.getClassLoader()
                .getResourceAsStream("config/application.properties")) {
            if (Objects.nonNull(stream)) {
                properties.load(stream);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load config/application.properties", e);
        }
        return properties;
    }
}
