package tests;

import config.ConfigManager;
import config.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import pages.HomePage;
import pages.LoginPage;


/**
 * BaseTest - Base class for all test classes
 * Provides common setup and teardown functionality
 */
public class BaseTest {
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);
    
    // Page Objects
    protected LoginPage loginPage;
    protected HomePage homePage;
    
    // Test data
    protected String testEmail;
    protected String testPassword;
    protected String testName;
    protected String testString;
    
    /**
     * Suite level setup - executed once before all tests
     */
    @BeforeSuite
    public void suiteSetup() {
        logger.info("========================================");
        logger.info("AGRICHAIN AUTOMATION TEST SUITE STARTED");
        logger.info("========================================");
        logger.info("Browser: {}", ConfigManager.getBrowser());
        logger.info("Environment: {}", ConfigManager.getProperty("environment", "local"));
        logger.info("Base URL: {}", ConfigManager.getBaseUrl());
    }
    
    /**
     * Method level setup - executed before each test method
     */
    @BeforeMethod
    public void setUp() {
        logger.info("Setting up test...");
        
        try {
            // Initialize WebDriver
            DriverManager.initializeDriver();
            logger.info("WebDriver initialized successfully");
            
            // Wait a moment to ensure driver is fully initialized
            Thread.sleep(500);
            
            // Initialize page objects
            initializePageObjects();
            
            // Load test data
            loadTestData();
            
            logger.info("Test setup completed");
        } catch (Exception e) {
            logger.error("Error during test setup: {}", e.getMessage());
            throw new RuntimeException("Test setup failed", e);
        }
    }
    
    /**
     * Method level teardown - executed after each test method
     */
    @AfterMethod
    public void tearDown() {
        logger.info("Tearing down test...");
        
        try {
            // Quit WebDriver
            DriverManager.quitDriver();
            logger.info("WebDriver quit successfully");
        } catch (Exception e) {
            logger.error("Error during teardown: {}", e.getMessage());
        }
        
        logger.info("Test teardown completed");
        logger.info("----------------------------------------");
    }
    
    /**
     * Initialize all page objects
     */
    private void initializePageObjects() {
        logger.debug("Initializing page objects...");
        
        try {
            // Ensure driver is available before creating page objects
            if (!DriverManager.isDriverInitialized()) {
                throw new RuntimeException("WebDriver is not initialized");
            }
            
            loginPage = new LoginPage();
            homePage = new HomePage();
            
            logger.debug("Page objects initialized successfully");
        } catch (Exception e) {
            logger.error("Failed to initialize page objects: {}", e.getMessage());
            throw new RuntimeException("Page object initialization failed", e);
        }
    }
    
    /**
     * Load test data from configuration
     */
    private void loadTestData() {
        logger.debug("Loading test data...");
        
        testEmail = ConfigManager.getTestEmail();
        testPassword = ConfigManager.getTestPassword();
        testName = ConfigManager.getTestName();
        testString = ConfigManager.getTestString();
        
        logger.debug("Test data loaded - Email: {}, Name: {}, Test String: {}", 
                testEmail, testName, testString);
    }
    
    /**
     * Navigate to login page
     */
    protected void navigateToLoginPage() {
        logger.info("Navigating to login page");
        loginPage.navigateToLoginPage();
    }
    
    /**
     * Navigate to home page
     */
    protected void navigateToHomePage() {
        logger.info("Navigating to home page");
        homePage.navigateToHomePage();
    }
    

    
    /**
     * Perform login with existing registered user
     * @return true if login was successful
     */
    protected boolean performLogin() {
        logger.info("=== PERFORMING LOGIN ===");
        logger.info("Using existing registered credentials - Email: {}", testEmail);
        
        try {
            // Navigate to login page
            logger.info("Navigating to login page");
            navigateToLoginPage();
            
            // Verify we're on login page
            if (!loginPage.isPageLoaded()) {
                logger.error("Login page failed to load");
                return false;
            }
            
            // Perform login with existing credentials
            logger.info("Attempting login with registered user: {}", testEmail);
            boolean loginSuccess = loginPage.performCompleteLogin(testEmail, testPassword);
            
            if (loginSuccess) {
                // Verify we're actually on home page
                String currentUrl = getCurrentUrl();
                if (currentUrl.contains("home")) {
                    logger.info("✅ Login successful and redirected to home page: {}", currentUrl);
                    return true;
                } else {
                    logger.warn("Login successful but not on home page. Current URL: {}", currentUrl);
                    return false;
                }
            } else {
                logger.error("❌ Login failed for user: {}", testEmail);
                
                // Try to get error message
                String loginMessage = loginPage.getMessageText();
                logger.error("Login error message: '{}'", loginMessage);
                return false;
            }
            
        } catch (Exception e) {
            logger.error("Exception during login: {}", e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Perform logout
     * @return true if logout was successful
     */
    protected boolean performLogout() {
        logger.info("Performing logout");
        
        boolean logoutSuccess = homePage.performLogout();
        
        if (logoutSuccess) {
            logger.info("Logout completed successfully");
        } else {
            logger.error("Logout failed");
        }
        
        return logoutSuccess;
    }
    
    /**
     * Submit string for processing
     * @param inputString String to process
     * @return true if submission was successful
     */
    protected boolean submitString(String inputString) {
        logger.info("Submitting string for processing: {}", inputString);
        
        boolean submissionSuccess = homePage.submitStringAndWaitForResults(inputString);
        
        if (submissionSuccess) {
            logger.info("String submission completed successfully");
        } else {
            logger.error("String submission failed");
        }
        
        return submissionSuccess;
    }
    
    /**
     * Get current page URL
     * @return Current URL
     */
    protected String getCurrentUrl() {
        return DriverManager.getCurrentUrl();
    }
    
    /**
     * Get current page title
     * @return Current page title
     */
    protected String getCurrentPageTitle() {
        return DriverManager.getPageTitle();
    }
    
    /**
     * Take screenshot (for failure reporting)
     * @param testName Test name for screenshot file
     */
    protected void takeScreenshot(String testName) {
        // Screenshot implementation would go here
        logger.info("Screenshot captured for test: {}", testName);
    }
    

    
    /**
     * Wait for specified duration
     * @param milliseconds Duration in milliseconds
     */
    protected void waitFor(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Wait interrupted: {}", e.getMessage());
        }
    }
} 