package config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigManager - Centralized configuration management
 * Handles reading and providing configuration properties
 */
public class ConfigManager {
    private static final Logger logger = LogManager.getLogger(ConfigManager.class);
    private static Properties properties;
    private static final String CONFIG_FILE = "config.properties";
    
    static {
        loadProperties();
    }
    
    /**
     * Load properties from config.properties file
     */
    private static void loadProperties() {
        properties = new Properties();
        try (InputStream inputStream = ConfigManager.class.getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {
            
            if (inputStream != null) {
                properties.load(inputStream);
                logger.info("Configuration properties loaded successfully");
                logger.debug("Loaded {} properties", properties.size());
                
                // Debug: Show browser property specifically
                String browserValue = properties.getProperty("browser");
                logger.debug("Browser property from file: '{}' (length: {})", 
                        browserValue, browserValue != null ? browserValue.length() : "null");
            } else {
                logger.error("Configuration file not found: " + CONFIG_FILE);
                throw new RuntimeException("Configuration file not found: " + CONFIG_FILE);
            }
        } catch (IOException e) {
            logger.error("Error loading configuration properties: " + e.getMessage());
            throw new RuntimeException("Error loading configuration properties", e);
        }
    }
    
    /**
     * Get property value by key
     * @param key Property key
     * @return Property value
     */
    public static String getProperty(String key) {
        String systemValue = System.getProperty(key);
        String fileValue = properties.getProperty(key);
        
        logger.debug("Getting property '{}': system='{}', file='{}'", key, systemValue, fileValue);
        
        String value = systemValue != null ? systemValue : fileValue;
        
        if (value != null) {
            value = value.trim(); // Trim whitespace
        }
        
        return value;
    }
    
    /**
     * Get property value with default fallback
     * @param key Property key
     * @param defaultValue Default value if property not found
     * @return Property value or default value
     */
    public static String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
    }
    
    /**
     * Get integer property value
     * @param key Property key
     * @return Integer value
     */
    public static int getIntProperty(String key) {
        String value = getProperty(key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.error("Invalid integer property: " + key + " = " + value);
            throw new RuntimeException("Invalid integer property: " + key, e);
        }
    }
    
    /**
     * Get boolean property value
     * @param key Property key
     * @return Boolean value
     */
    public static boolean getBooleanProperty(String key) {
        String value = getProperty(key);
        return Boolean.parseBoolean(value);
    }
    
    // Commonly used configuration getters
    public static String getBrowser() {
        String browser = getProperty("browser", "chrome");
        // Handle case where system property is set but empty
        if (browser == null || browser.trim().isEmpty()) {
            browser = "chrome";
        }
        return browser.trim();
    }
    
    public static String getBaseUrl() {
        return getProperty("base.url");
        // TODO: Add validation for file:// URLs to ensure they exist
    }
    
    public static String getHomeUrl() {
        return getProperty("home.url");
    }
    
    public static String getRegisterUrl() {
        return getProperty("register.url");
    }
    
    public static String getResultUrl() {
        return getProperty("result.url");
    }
    
    public static int getImplicitWait() {
        return getIntProperty("implicit.wait");
    }
    
    public static int getExplicitWait() {
        return getIntProperty("explicit.wait");
    }
    
    public static int getPageLoadTimeout() {
        return getIntProperty("page.load.timeout");
    }
    
    public static boolean isHeadless() {
        return getBooleanProperty("headless");
    }
    
    public static boolean shouldMaximize() {
        return getBooleanProperty("maximize");
    }
    
    public static boolean shouldTakeScreenshotOnFailure() {
        return getBooleanProperty("screenshot.on.failure");
    }
    
    public static String getScreenshotPath() {
        return getProperty("screenshot.path");
    }
    
    public static String getTestEmail() {
        return getProperty("test.email");
    }
    
    public static String getTestPassword() {
        return getProperty("test.password");
    }
    
    public static String getTestName() {
        return getProperty("test.name");
    }
    
    public static String getTestString() {
        return getProperty("test.string");
    }
    
    public static int getWindowWidth() {
        return getIntProperty("window.width");
    }
    
    public static int getWindowHeight() {
        return getIntProperty("window.height");
    }
} 