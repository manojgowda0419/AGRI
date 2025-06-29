package utils;

import config.ConfigManager;
import config.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * WaitUtils - Utility class for explicit waits and element conditions
 * Provides reusable wait methods for better element handling
 */
public class WaitUtils {
    private static final Logger logger = LogManager.getLogger(WaitUtils.class);
    private static final int DEFAULT_TIMEOUT = ConfigManager.getExplicitWait();
    
    /**
     * Get WebDriverWait instance with default timeout
     * @return WebDriverWait instance
     */
    private static WebDriverWait getWait() {
        return new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(DEFAULT_TIMEOUT));
    }
    
    /**
     * Get WebDriverWait instance with custom timeout
     * @param timeoutInSeconds Custom timeout in seconds
     * @return WebDriverWait instance
     */
    private static WebDriverWait getWait(int timeoutInSeconds) {
        return new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutInSeconds));
    }
    
    /**
     * Wait for element to be visible
     * @param locator Element locator
     * @return WebElement when visible
     */
    public static WebElement waitForElementToBeVisible(By locator) {
        logger.debug("Waiting for element to be visible: {}", locator);
        try {
            WebElement element = getWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
            logger.debug("Element is now visible: {}", locator);
            return element;
        } catch (Exception e) {
            logger.error("Element not visible within {} seconds: {}", DEFAULT_TIMEOUT, locator);
            throw e;
        }
    }
    
    /**
     * Wait for element to be visible with custom timeout
     * @param locator Element locator
     * @param timeoutInSeconds Custom timeout
     * @return WebElement when visible
     */
    public static WebElement waitForElementToBeVisible(By locator, int timeoutInSeconds) {
        logger.debug("Waiting for element to be visible: {} (timeout: {}s)", locator, timeoutInSeconds);
        try {
            WebElement element = getWait(timeoutInSeconds).until(ExpectedConditions.visibilityOfElementLocated(locator));
            logger.debug("Element is now visible: {}", locator);
            return element;
        } catch (Exception e) {
            logger.error("Element not visible within {} seconds: {}", timeoutInSeconds, locator);
            throw e;
        }
    }
    
    /**
     * Wait for element to be clickable
     * @param locator Element locator
     * @return WebElement when clickable
     */
    public static WebElement waitForElementToBeClickable(By locator) {
        logger.debug("Waiting for element to be clickable: {}", locator);
        try {
            WebElement element = getWait().until(ExpectedConditions.elementToBeClickable(locator));
            logger.debug("Element is now clickable: {}", locator);
            return element;
        } catch (Exception e) {
            logger.error("Element not clickable within {} seconds: {}", DEFAULT_TIMEOUT, locator);
            throw e;
        }
    }
    
    /**
     * Wait for element to be clickable with custom timeout
     * @param locator Element locator
     * @param timeoutInSeconds Custom timeout
     * @return WebElement when clickable
     */
    public static WebElement waitForElementToBeClickable(By locator, int timeoutInSeconds) {
        logger.debug("Waiting for element to be clickable: {} (timeout: {}s)", locator, timeoutInSeconds);
        try {
            WebElement element = getWait(timeoutInSeconds).until(ExpectedConditions.elementToBeClickable(locator));
            logger.debug("Element is now clickable: {}", locator);
            return element;
        } catch (Exception e) {
            logger.error("Element not clickable within {} seconds: {}", timeoutInSeconds, locator);
            throw e;
        }
    }
    
    /**
     * Wait for element to be present in DOM
     * @param locator Element locator
     * @return WebElement when present
     */
    public static WebElement waitForElementToBePresent(By locator) {
        logger.debug("Waiting for element to be present: {}", locator);
        try {
            WebElement element = getWait().until(ExpectedConditions.presenceOfElementLocated(locator));
            logger.debug("Element is now present: {}", locator);
            return element;
        } catch (Exception e) {
            logger.error("Element not present within {} seconds: {}", DEFAULT_TIMEOUT, locator);
            throw e;
        }
    }
    
    /**
     * Wait for element to be invisible
     * @param locator Element locator
     * @return true when element is invisible
     */
    public static boolean waitForElementToBeInvisible(By locator) {
        logger.debug("Waiting for element to be invisible: {}", locator);
        try {
            boolean invisible = getWait().until(ExpectedConditions.invisibilityOfElementLocated(locator));
            logger.debug("Element is now invisible: {}", locator);
            return invisible;
        } catch (Exception e) {
            logger.error("Element still visible after {} seconds: {}", DEFAULT_TIMEOUT, locator);
            throw e;
        }
    }
    
    /**
     * Wait for text to be present in element
     * @param locator Element locator
     * @param text Expected text
     * @return true when text is present
     */
    public static boolean waitForTextToBePresentInElement(By locator, String text) {
        logger.debug("Waiting for text '{}' to be present in element: {}", text, locator);
        try {
            boolean textPresent = getWait().until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
            logger.debug("Text '{}' is now present in element: {}", text, locator);
            return textPresent;
        } catch (Exception e) {
            logger.error("Text '{}' not present in element within {} seconds: {}", text, DEFAULT_TIMEOUT, locator);
            throw e;
        }
    }
    
    /**
     * Wait for attribute to contain specific value
     * @param locator Element locator
     * @param attribute Attribute name
     * @param value Expected value
     * @return true when attribute contains value
     */
    public static boolean waitForAttributeToContain(By locator, String attribute, String value) {
        logger.debug("Waiting for attribute '{}' to contain '{}' in element: {}", attribute, value, locator);
        try {
            boolean attributeContains = getWait().until(
                ExpectedConditions.attributeContains(locator, attribute, value)
            );
            logger.debug("Attribute '{}' now contains '{}' in element: {}", attribute, value, locator);
            return attributeContains;
        } catch (Exception e) {
            logger.error("Attribute '{}' does not contain '{}' within {} seconds: {}", 
                    attribute, value, DEFAULT_TIMEOUT, locator);
            throw e;
        }
    }
    
    /**
     * Wait for URL to contain specific text
     * @param urlFragment URL fragment to wait for
     * @return true when URL contains fragment
     */
    public static boolean waitForUrlToContain(String urlFragment) {
        logger.debug("Waiting for URL to contain: {}", urlFragment);
        try {
            boolean urlContains = getWait().until(ExpectedConditions.urlContains(urlFragment));
            logger.debug("URL now contains: {}", urlFragment);
            return urlContains;
        } catch (Exception e) {
            logger.error("URL does not contain '{}' within {} seconds", urlFragment, DEFAULT_TIMEOUT);
            throw e;
        }
    }
    
    /**
     * Wait for page title to contain specific text
     * @param title Expected title text
     * @return true when title contains text
     */
    public static boolean waitForTitleToContain(String title) {
        logger.debug("Waiting for page title to contain: {}", title);
        try {
            boolean titleContains = getWait().until(ExpectedConditions.titleContains(title));
            logger.debug("Page title now contains: {}", title);
            return titleContains;
        } catch (Exception e) {
            logger.error("Page title does not contain '{}' within {} seconds", title, DEFAULT_TIMEOUT);
            throw e;
        }
    }
    
    /**
     * Sleep for specified milliseconds
     * @param milliseconds Sleep duration
     */
    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Sleep interrupted: {}", e.getMessage());
        }
    }
} 