package pages;

import config.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import utils.WaitUtils;

/**
 * BasePage - Base class for all page objects
 * Provides common functionality and utilities for page interactions
 */
public abstract class BasePage {
    protected static final Logger logger = LogManager.getLogger(BasePage.class);
    protected WebDriver driver;
    
    /**
     * Constructor to initialize page
     */
    public BasePage() {
        this.driver = DriverManager.getDriver();
        PageFactory.initElements(driver, this);
        logger.debug("Initialized page: {}", this.getClass().getSimpleName());
    }
    
    /**
     * Navigate to specific URL
     * @param url URL to navigate to
     */
    protected void navigateToUrl(String url) {
        logger.info("Navigating to URL: {}", url);
        driver.get(url);
        waitForPageToLoad();
    }
    
    /**
     * Get current page URL
     * @return Current URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    
    /**
     * Get page title
     * @return Page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }
    
    /**
     * Wait for page to load (can be overridden by specific pages)
     */
    protected void waitForPageToLoad() {
        // Default implementation - can be overridden by specific pages
        WaitUtils.sleep(1000);
    }
    
    /**
     * Find element with explicit wait
     * @param locator Element locator
     * @return WebElement
     */
    protected WebElement findElement(By locator) {
        return WaitUtils.waitForElementToBeVisible(locator);
    }
    
    /**
     * Click element with explicit wait
     * @param locator Element locator
     */
    protected void clickElement(By locator) {
        logger.debug("Clicking element: {}", locator);
        WebElement element = WaitUtils.waitForElementToBeClickable(locator);
        element.click();
        logger.debug("Successfully clicked element: {}", locator);
    }
    
    /**
     * Enter text into input field
     * @param locator Element locator
     * @param text Text to enter
     */
    protected void enterText(By locator, String text) {
        logger.debug("Entering text '{}' into element: {}", text, locator);
        WebElement element = WaitUtils.waitForElementToBeVisible(locator);
        element.clear();
        element.sendKeys(text);
        logger.debug("Successfully entered text into element: {}", locator);
    }
    
    /**
     * Get text from element
     * @param locator Element locator
     * @return Element text
     */
    protected String getText(By locator) {
        logger.debug("Getting text from element: {}", locator);
        WebElement element = WaitUtils.waitForElementToBeVisible(locator);
        String text = element.getText();
        logger.debug("Retrieved text '{}' from element: {}", text, locator);
        return text;
    }
    
    /**
     * Get attribute value from element
     * @param locator Element locator
     * @param attribute Attribute name
     * @return Attribute value
     */
    protected String getAttribute(By locator, String attribute) {
        logger.debug("Getting attribute '{}' from element: {}", attribute, locator);
        WebElement element = WaitUtils.waitForElementToBeVisible(locator);
        String attributeValue = element.getAttribute(attribute);
        logger.debug("Retrieved attribute '{}' = '{}' from element: {}", attribute, attributeValue, locator);
        return attributeValue;
    }
    
    /**
     * Check if element is displayed
     * @param locator Element locator
     * @return true if element is displayed
     */
    protected boolean isElementDisplayed(By locator) {
        try {
            WebElement element = WaitUtils.waitForElementToBeVisible(locator, 5);
            boolean displayed = element.isDisplayed();
            logger.debug("Element {} display status: {}", locator, displayed);
            return displayed;
        } catch (Exception e) {
            logger.debug("Element {} is not displayed", locator);
            return false;
        }
    }
    
    /**
     * Check if element is enabled
     * @param locator Element locator
     * @return true if element is enabled
     */
    protected boolean isElementEnabled(By locator) {
        try {
            WebElement element = WaitUtils.waitForElementToBePresent(locator);
            boolean enabled = element.isEnabled();
            logger.debug("Element {} enabled status: {}", locator, enabled);
            return enabled;
        } catch (Exception e) {
            logger.debug("Element {} is not enabled", locator);
            return false;
        }
    }
    
    /**
     * Wait for element to be invisible
     * @param locator Element locator
     * @return true when element is invisible
     */
    protected boolean waitForElementToDisappear(By locator) {
        logger.debug("Waiting for element to disappear: {}", locator);
        return WaitUtils.waitForElementToBeInvisible(locator);
    }
    
    /**
     * Wait for text to be present in element
     * @param locator Element locator
     * @param expectedText Expected text
     * @return true when text is present
     */
    protected boolean waitForTextToBePresentInElement(By locator, String expectedText) {
        logger.debug("Waiting for text '{}' in element: {}", expectedText, locator);
        return WaitUtils.waitForTextToBePresentInElement(locator, expectedText);
    }
    
    /**
     * Scroll element into view
     * @param locator Element locator
     */
    protected void scrollToElement(By locator) {
        logger.debug("Scrolling to element: {}", locator);
        WebElement element = WaitUtils.waitForElementToBePresent(locator);
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);", element);
        logger.debug("Scrolled to element: {}", locator);
    }
    
    /**
     * Refresh the current page
     */
    protected void refreshPage() {
        logger.info("Refreshing page");
        driver.navigate().refresh();
        waitForPageToLoad();
    }
    
    /**
     * Navigate back in browser history
     */
    protected void navigateBack() {
        logger.info("Navigating back");
        driver.navigate().back();
        waitForPageToLoad();
    }
    
    /**
     * Navigate forward in browser history
     */
    protected void navigateForward() {
        logger.info("Navigating forward");
        driver.navigate().forward();
        waitForPageToLoad();
    }
    
    /**
     * Abstract method to verify page is loaded
     * Must be implemented by each page class
     * @return true if page is loaded correctly
     */
    public abstract boolean isPageLoaded();
} 