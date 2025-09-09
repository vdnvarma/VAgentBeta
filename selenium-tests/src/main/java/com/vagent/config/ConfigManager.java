package com.vagent.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Configuration manager to handle application properties
 */
public class ConfigManager {
    private static Properties properties = new Properties();
    private static final String CONFIG_FILE_PATH = "src/test/resources/config.properties";

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try {
            FileInputStream fileInputStream = new FileInputStream(CONFIG_FILE_PATH);
            properties.load(fileInputStream);
            fileInputStream.close();
        } catch (IOException e) {
            System.err.println("Failed to load config properties: " + e.getMessage());
            // Set default values
            setDefaultProperties();
        }
    }

    private static void setDefaultProperties() {
        // Use environment variables or fallback to defaults
        properties.setProperty("base.url", System.getenv("BASE_URL") != null ? 
            System.getenv("BASE_URL") : "https://localhost:3000");
        properties.setProperty("browser", System.getenv("BROWSER") != null ? 
            System.getenv("BROWSER") : "chrome");
        properties.setProperty("implicit.wait", "30");
        properties.setProperty("explicit.wait", "60");
        properties.setProperty("page.load.timeout", "120");
        properties.setProperty("test.email", System.getenv("TEST_EMAIL") != null ? 
            System.getenv("TEST_EMAIL") : "test@example.com");
        properties.setProperty("test.password", System.getenv("TEST_PASSWORD") != null ? 
            System.getenv("TEST_PASSWORD") : "password123");
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getBaseUrl() {
        return properties.getProperty("base.url");
    }

    public static String getBrowser() {
        return properties.getProperty("browser");
    }

    public static int getImplicitWait() {
        return Integer.parseInt(properties.getProperty("implicit.wait"));
    }

    public static int getExplicitWait() {
        return Integer.parseInt(properties.getProperty("explicit.wait"));
    }

    public static int getPageLoadTimeout() {
        return Integer.parseInt(properties.getProperty("page.load.timeout"));
    }

    public static String getTestEmail() {
        return properties.getProperty("test.email");
    }

    public static String getTestPassword() {
        return properties.getProperty("test.password");
    }
}
