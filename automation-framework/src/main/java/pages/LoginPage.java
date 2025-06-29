package pages;

import config.ConfigManager;
import utils.WaitUtils;
import org.openqa.selenium.By;

/**
 * LoginPage - Page Object for user login page
 * Contains all elements and methods for login functionality
 */
public class LoginPage extends BasePage {
    
    // Page Elements
    private final By emailField = By.id("email");
    private final By passwordField = By.id("password");
    private final By loginButton = By.xpath("//button[@type='submit']");
    private final By messageArea = By.id("loginMessage");

    private final By siteTitle = By.className("site-title");
    private final By authLinks = By.className("auth-links");
    
    // Success/Error message selectors
    private final By successMessage = By.xpath("//div[contains(@class,'success')]");
    private final By errorMessage = By.xpath("//div[contains(@class,'error')]");
    
    /**
     * Navigate to login page
     */
    public void navigateToLoginPage() {
        String loginUrl = ConfigManager.getBaseUrl();
        logger.info("Navigating to login page: {}", loginUrl);
        navigateToUrl(loginUrl);
    }
    
    /**
     * Wait for login page to load
     */
    @Override
    protected void waitForPageToLoad() {
        WaitUtils.waitForElementToBeVisible(loginButton);
        logger.info("Login page loaded successfully");
    }
    
    /**
     * Check if login page is loaded
     * @return true if page is loaded
     */
    @Override
    public boolean isPageLoaded() {
        try {
            return isElementDisplayed(loginButton) && 
                   isElementDisplayed(emailField) &&
                   isElementDisplayed(passwordField) &&
                   getCurrentUrl().contains("index");
        } catch (Exception e) {
            logger.error("Login page not loaded properly: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Enter email address
     * @param email Email address
     */
    public void enterEmail(String email) {
        logger.info("Entering email: {}", email);
        enterText(emailField, email);
    }
    
    /**
     * Enter password
     * @param password Password
     */
    public void enterPassword(String password) {
        logger.info("Entering password");
        enterText(passwordField, password);
    }
    
    /**
     * Click login button
     */
    public void clickLoginButton() {
        logger.info("Clicking login button");
        clickElement(loginButton);
    }
    

    
    /**
     * Get message text from message area
     * @return Message text
     */
    public String getMessageText() {
        try {
            if (isElementDisplayed(successMessage)) {
                return getText(successMessage);
            } else if (isElementDisplayed(errorMessage)) {
                return getText(errorMessage);
            } else if (isElementDisplayed(messageArea)) {
                return getText(messageArea);
            }
            return "";
        } catch (Exception e) {
            logger.warn("No message found on login page");
            return "";
        }
    }
    
    /**
     * Check if success message is displayed
     * @return true if success message is visible
     */
    public boolean isSuccessMessageDisplayed() {
        return isElementDisplayed(successMessage) || 
               (isElementDisplayed(messageArea) && getText(messageArea).contains("successful"));
    }
    
    /**
     * Check if error message is displayed
     * @return true if error message is visible
     */
    public boolean isErrorMessageDisplayed() {
        return isElementDisplayed(errorMessage) || 
               (isElementDisplayed(messageArea) && getText(messageArea).contains("Invalid"));
    }
    
    /**
     * Wait for success message to appear
     * @return true if success message appears
     */
    public boolean waitForSuccessMessage() {
        try {
            return waitForTextToBePresentInElement(messageArea, "successful") ||
                   WaitUtils.waitForElementToBeVisible(successMessage) != null;
        } catch (Exception e) {
            logger.warn("Success message did not appear");
            return false;
        }
    }
    
    /**
     * Wait for error message to appear
     * @return true if error message appears
     */
    public boolean waitForErrorMessage() {
        try {
            return isElementDisplayed(errorMessage) || isElementDisplayed(messageArea);
        } catch (Exception e) {
            logger.warn("Error message did not appear");
            return false;
        }
    }
    
    /**
     * Fill login form
     * @param email Email address
     * @param password Password
     */
    public void fillLoginForm(String email, String password) {
        logger.info("Filling login form for user: {}", email);
        enterEmail(email);
        enterPassword(password);
    }
    
    /**
     * Complete user login process
     * @param email Email address
     * @param password Password
     * @return true if login was successful
     */
    public boolean loginUser(String email, String password) {
        logger.info("Starting user login process for: {}", email);
        
        fillLoginForm(email, password);
        clickLoginButton();
        
        // Wait for response and check if redirected to home page
        WaitUtils.sleep(2000); // Give time for redirect
        
        boolean isSuccessful = getCurrentUrl().contains("home") || isSuccessMessageDisplayed();
        logger.info("Login {} for user: {}", 
                isSuccessful ? "successful" : "failed", email);
        
        return isSuccessful;
    }
    
    /**
     * Login user with default test data
     * @return true if login was successful
     */
    public boolean loginUserWithTestData() {
        return loginUser(
                ConfigManager.getTestEmail(),
                ConfigManager.getTestPassword()
        );
    }
    
    /**
     * Clear login form fields
     */
    public void clearForm() {
        logger.info("Clearing login form");
        enterText(emailField, "");
        enterText(passwordField, "");
    }
    
    /**
     * Get login page title
     * @return Login page title
     */
    public String getLoginPageTitle() {
        return getPageTitle();
    }
    
    /**
     * Verify login page elements are present
     * @return true if all required elements are present
     */
    public boolean verifyPageElements() {
        logger.info("Verifying login page elements");
        return isElementDisplayed(emailField) &&
               isElementDisplayed(passwordField) &&
               isElementDisplayed(loginButton) &&
               isElementDisplayed(siteTitle);
    }
    
    /**
     * Check if user is already logged in (by checking if already redirected)
     * @return true if user appears to be logged in
     */
    public boolean isUserAlreadyLoggedIn() {
        // If we're redirected to home page, user is likely already logged in
        return getCurrentUrl().contains("home");
    }
    
    /**
     * Wait for login to complete (redirect to home page)
     * @return true if redirected to home page
     */
    public boolean waitForLoginSuccess() {
        try {
            return WaitUtils.waitForUrlToContain("home");
        } catch (Exception e) {
            logger.warn("Login redirect to home page did not occur");
            return false;
        }
    }
    
    /**
     * Attempt login and wait for result
     * @param email Email address
     * @param password Password
     * @return true if successfully logged in and redirected to home
     */
    public boolean performCompleteLogin(String email, String password) {
        logger.info("Performing complete login for user: {}", email);
        
        // Fill form and submit
        fillLoginForm(email, password);
        clickLoginButton();
        
        // Wait for either success redirect or error message
        boolean loginSuccessful = waitForLoginSuccess();
        
        if (!loginSuccessful) {
            // Check if there's an error message
            waitForErrorMessage();
            String errorMsg = getMessageText();
            logger.warn("Login failed for user {}: {}", email, errorMsg);
        }
        
        return loginSuccessful;
    }
} 