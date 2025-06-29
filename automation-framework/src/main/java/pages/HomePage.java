package pages;

import config.ConfigManager;
import utils.WaitUtils;
import org.openqa.selenium.By;

/**
 * HomePage - Page Object for home page (string input page)
 * Contains all elements and methods for string input and calculation functionality
 */
public class HomePage extends BasePage {
    
    // Page Elements
    private final By stringInputField = By.id("stringInput");
    private final By submitButton = By.xpath("//button[@type='submit']");
    private final By userEmailDisplay = By.id("userEmail");
    private final By logoutButton = By.id("logoutBtn");
    private final By siteTitle = By.className("site-title");
    private final By agrichainTitle = By.xpath("//div[contains(text(),'Agrichain')]");
    private final By inputForm = By.id("stringForm");
    
    // Navigation elements
    private final By websiteHeader = By.className("website-header");
    private final By mainContent = By.className("main-content");
    
    /**
     * Navigate to home page
     */
    public void navigateToHomePage() {
        String homeUrl = ConfigManager.getHomeUrl();
        logger.info("Navigating to home page: {}", homeUrl);
        navigateToUrl(homeUrl);
    }
    
    /**
     * Wait for home page to load
     */
    @Override
    protected void waitForPageToLoad() {
        WaitUtils.waitForElementToBeVisible(stringInputField);
        WaitUtils.waitForElementToBeVisible(submitButton);
        logger.info("Home page loaded successfully");
    }
    
    /**
     * Check if home page is loaded
     * @return true if page is loaded
     */
    @Override
    public boolean isPageLoaded() {
        try {
            return isElementDisplayed(stringInputField) && 
                   isElementDisplayed(submitButton) &&
                   isElementDisplayed(userEmailDisplay) &&
                   getCurrentUrl().contains("home");
        } catch (Exception e) {
            logger.error("Home page not loaded properly: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Enter string input for calculation
     * @param inputString String to be processed
     */
    public void enterStringInput(String inputString) {
        logger.info("Entering string input: {}", inputString);
        enterText(stringInputField, inputString);
    }
    
    /**
     * Click submit button to process string
     */
    public void clickSubmitButton() {
        logger.info("Clicking submit button");
        clickElement(submitButton);
    }
    
    /**
     * Click logout button
     */
    public void clickLogoutButton() {
        logger.info("Clicking logout button");
        clickElement(logoutButton);
    }
    
    /**
     * Get displayed user email
     * @return User email displayed on page
     */
    public String getDisplayedUserEmail() {
        try {
            String email = getText(userEmailDisplay);
            logger.info("Displayed user email: {}", email);
            return email;
        } catch (Exception e) {
            logger.warn("Could not retrieve user email: {}", e.getMessage());
            return "";
        }
    }
    
    /**
     * Get current string input value
     * @return Current value in string input field
     */
    public String getCurrentStringInput() {
        try {
            String value = getAttribute(stringInputField, "value");
            logger.debug("Current string input value: {}", value);
            return value;
        } catch (Exception e) {
            logger.warn("Could not retrieve string input value: {}", e.getMessage());
            return "";
        }
    }
    
    /**
     * Clear string input field
     */
    public void clearStringInput() {
        logger.info("Clearing string input field");
        enterText(stringInputField, "");
    }
    
    /**
     * Check if submit button is enabled
     * @return true if submit button is enabled
     */
    public boolean isSubmitButtonEnabled() {
        return isElementEnabled(submitButton);
    }
    
    /**
     * Check if logout button is visible
     * @return true if logout button is visible
     */
    public boolean isLogoutButtonVisible() {
        return isElementDisplayed(logoutButton);
    }
    
    /**
     * Check if user email is displayed (indicating user is logged in)
     * @return true if user email is displayed
     */
    public boolean isUserEmailDisplayed() {
        return isElementDisplayed(userEmailDisplay) && !getText(userEmailDisplay).isEmpty();
    }
    
    /**
     * Submit string for processing
     * @param inputString String to process
     * @return true if submission was successful (redirected to results)
     */
    public boolean submitStringForProcessing(String inputString) {
        logger.info("Submitting string for processing: {}", inputString);
        
        enterStringInput(inputString);
        clickSubmitButton();
        
        // Wait for redirect to results page
        WaitUtils.sleep(2000);
        
        boolean successful = getCurrentUrl().contains("result");
        logger.info("String submission {}", successful ? "successful" : "failed");
        
        return successful;
    }
    
    /**
     * Submit string and wait for results page
     * @param inputString String to process
     * @return true if redirected to results page
     */
    public boolean submitStringAndWaitForResults(String inputString) {
        logger.info("Submitting string and waiting for results: {}", inputString);
        
        enterStringInput(inputString);
        clickSubmitButton();
        
        try {
            return WaitUtils.waitForUrlToContain("result");
        } catch (Exception e) {
            logger.warn("Results page did not load: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Submit test string from configuration
     * @return true if submission was successful
     */
    public boolean submitTestString() {
        String testString = ConfigManager.getTestString();
        return submitStringForProcessing(testString);
    }
    
    /**
     * Perform logout action
     * @return true if logout was successful (redirected to login page)
     */
    public boolean performLogout() {
        logger.info("Performing logout");
        
        clickLogoutButton();
        
        // Wait for redirect to login page
        WaitUtils.sleep(2000);
        
        boolean successful = getCurrentUrl().contains("index") || getCurrentUrl().contains("login");
        logger.info("Logout {}", successful ? "successful" : "failed");
        
        return successful;
    }
    
    /**
     * Wait for logout to complete (redirect to login page)
     * @return true if redirected to login page
     */
    public boolean waitForLogoutSuccess() {
        try {
            return WaitUtils.waitForUrlToContain("index") || WaitUtils.waitForUrlToContain("login");
        } catch (Exception e) {
            logger.warn("Logout redirect did not occur: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Verify home page elements are present
     * @return true if all required elements are present
     */
    public boolean verifyPageElements() {
        logger.info("Verifying home page elements");
        return isElementDisplayed(stringInputField) &&
               isElementDisplayed(submitButton) &&
               isElementDisplayed(userEmailDisplay) &&
               isElementDisplayed(logoutButton) &&
               isElementDisplayed(siteTitle);
    }
    
    /**
     * Verify user is logged in by checking displayed elements
     * @return true if user appears to be logged in
     */
    public boolean verifyUserIsLoggedIn() {
        return isUserEmailDisplayed() && 
               isLogoutButtonVisible() && 
               !getDisplayedUserEmail().isEmpty();
    }
    
    /**
     * Get page title
     * @return Home page title
     */
    public String getHomePageTitle() {
        return getPageTitle();
    }
    
    /**
     * Check if string input field is empty
     * @return true if input field is empty
     */
    public boolean isStringInputEmpty() {
        return getCurrentStringInput().trim().isEmpty();
    }
    
    /**
     * Verify that form validation prevents empty submission
     * @return true if empty submission is prevented
     */
    public boolean verifyEmptySubmissionPrevention() {
        logger.info("Testing empty submission prevention");
        
        clearStringInput();
        clickSubmitButton();
        
        // Should stay on same page (not redirect to results)
        WaitUtils.sleep(1000);
        
        boolean staysOnHomePage = getCurrentUrl().contains("home");
        logger.info("Empty submission prevention: {}", staysOnHomePage ? "working" : "not working");
        
        return staysOnHomePage;
    }
    
    /**
     * Test string input with various test cases
     * @param testString Test string
     * @return true if input was accepted and processed
     */
    public boolean testStringInput(String testString) {
        logger.info("Testing string input: {}", testString);
        
        clearStringInput();
        enterStringInput(testString);
        
        // Verify input was entered correctly
        String enteredValue = getCurrentStringInput();
        boolean inputCorrect = testString.equals(enteredValue);
        
        if (!inputCorrect) {
            logger.warn("Input mismatch. Expected: '{}', Actual: '{}'", testString, enteredValue);
        }
        
        return inputCorrect;
    }
} 