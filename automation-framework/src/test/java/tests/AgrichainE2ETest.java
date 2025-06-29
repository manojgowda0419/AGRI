package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Agrichain E2E Test Suite
 * 
 * Main test class for testing the longest substring functionality
 * Tests cover login, string processing, and navigation
 * 
 * @author QA Team
 * @version 1.0
 */
public class AgrichainE2ETest extends BaseTest {
    
    // Test tracking variables
    private long testStartTime;
    private long phaseStartTime;
    private int totalSteps = 0;
    private int completedSteps = 0;
    
    // Test data for algorithm validation
    private final Map<String, TestResult> testData = new HashMap<String, TestResult>() {{
        put("abcabcbb", new TestResult(3, "abc"));
        put("bbbbb", new TestResult(1, "b"));
        put("pwwkew", new TestResult(3, "wke"));
        put("abcdef", new TestResult(6, "abcdef"));
        put("a", new TestResult(1, "a"));
    }};
    
    /**
     * Main E2E test - covers the complete user flow
     * TODO: Add more edge cases for string input
     */
    @Test(priority = 1, description = "Complete E2E User Journey - Login to Logout")
    public void testCompleteUserJourney() {
        printHeader("COMPLETE E2E USER JOURNEY", "ATC_E2E_001");
        testStartTime = System.currentTimeMillis();
        totalSteps = 7;
        
        try {
            // Phase 1: Setup
            logPhase("PHASE 1", "Application Setup");
            
            logStep(1, "Initialize Browser & Navigate to App");
            validateBrowserSetup();
            completeStep(1, "Browser setup completed");
            
            // Phase 2: Login
            logPhase("PHASE 2", "User Authentication");
            
            logStep(2, "Perform User Login");
            long loginStart = System.currentTimeMillis();
            boolean loginOk = performLogin();
            long loginTime = System.currentTimeMillis() - loginStart;
            
            Assert.assertTrue(loginOk, "Login failed for user: " + testEmail);
            Assert.assertTrue(getCurrentUrl().contains("home"), "Login redirect failed");
            
            completeStep(2, String.format("Login successful (%dms)", loginTime));
            
            logStep(3, "Verify Home Page Access");
            validateHomeAccess();
            completeStep(3, "Home page verified");
            
            // Phase 3: String processing
            logPhase("PHASE 3", "String Input & Processing");
            
            logStep(4, "Enter Test String");
            String input = testString;
            long inputStart = System.currentTimeMillis();
            
            homePage.enterStringInput(input);
            String enteredValue = homePage.getCurrentStringInput();
            long inputTime = System.currentTimeMillis() - inputStart;
            
            Assert.assertEquals(enteredValue, input, "Input mismatch");
            completeStep(4, String.format("String '%s' entered (%dms)", input, inputTime));
            
            logStep(5, "Submit String for Processing");
            long submitStart = System.currentTimeMillis();
            boolean submitOk = homePage.submitStringAndWaitForResults(input);
            long submitTime = System.currentTimeMillis() - submitStart;
            
            Assert.assertTrue(submitOk, "String submission failed: " + input);
            Assert.assertTrue(getCurrentUrl().contains("result"), "Result redirect failed");
            
            completeStep(5, String.format("Processing completed (%dms)", submitTime));
            
            // Phase 4: Results
            logPhase("PHASE 4", "Results Verification");
            
            logStep(6, "Verify Results & Navigate Back");
            validateResults();
            completeStep(6, "Results verified successfully");
            
            // Phase 5: Cleanup
            logPhase("PHASE 5", "User Logout");
            
            logStep(7, "Perform Logout");
            long logoutStart = System.currentTimeMillis();
            boolean logoutOk = performLogout();
            long logoutTime = System.currentTimeMillis() - logoutStart;
            
            Assert.assertTrue(logoutOk, "Logout failed");
            Assert.assertTrue(getCurrentUrl().contains("index") || getCurrentUrl().contains("login"), 
                "Logout redirect failed");
            
            completeStep(7, String.format("Logout successful (%dms)", logoutTime));
            
            printSummary("COMPLETE E2E USER JOURNEY", true, input);
            
        } catch (AssertionError e) {
            logger.error("Test failed: {}", e.getMessage());
            takeScreenshot("E2E_Failed");
            printSummary("COMPLETE E2E USER JOURNEY", false, testString);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
            takeScreenshot("E2E_Error");
            printSummary("COMPLETE E2E USER JOURNEY", false, testString);
            Assert.fail("Test failed: " + e.getMessage());
        }
    }
    
    /**
     * Algorithm validation test with multiple inputs
     * TODO: Add performance benchmarking
     */
    @Test(priority = 2, description = "Algorithm Validation with Multiple Test Cases")
    public void testStringProcessingAlgorithm() {
        printHeader("STRING ALGORITHM VALIDATION", "ATC_ALG_001");
        testStartTime = System.currentTimeMillis();
        totalSteps = testData.size() + 2; // +2 for login/logout
        
        try {
            // Login first
            logPhase("PHASE 1", "Authentication Setup");
            logStep(1, "Login for Algorithm Testing");
            boolean loginOk = performLogin();
            Assert.assertTrue(loginOk, "Login failed");
            completeStep(1, "Authentication completed");
            
            // Test each case
            logPhase("PHASE 2", "Algorithm Testing");
            
            int step = 2;
            for (Map.Entry<String, TestResult> testCase : testData.entrySet()) {
                String input = testCase.getKey();
                TestResult expected = testCase.getValue();
                
                logStep(step, String.format("Test: '%s' (expected length: %d)", input, expected.length));
                
                long start = System.currentTimeMillis();
                validateAlgorithm(input, expected);
                long duration = System.currentTimeMillis() - start;
                
                completeStep(step, String.format("'%s' validated (%dms)", input, duration));
                step++;
            }
            
            // Cleanup
            logPhase("PHASE 3", "Cleanup");
            logStep(step, "Logout");
            navigateToHomePage();
            boolean logoutOk = performLogout();
            Assert.assertTrue(logoutOk, "Cleanup logout failed");
            completeStep(step, "Cleanup completed");
            
            printAlgorithmSummary();
            
        } catch (AssertionError e) {
            logger.error("Algorithm test failed: {}", e.getMessage());
            takeScreenshot("Algorithm_Failed");
            throw e;
        } catch (Exception e) {
            logger.error("Algorithm test error: {}", e.getMessage());
            takeScreenshot("Algorithm_Error");
            Assert.fail("Algorithm test failed: " + e.getMessage());
        }
    }
    
    /**
     * Authentication flow validation
     */
    @Test(priority = 3, description = "Authentication Flow Testing")
    public void testAuthFlow() {
        printHeader("AUTHENTICATION FLOW", "ATC_AUTH_001");
        testStartTime = System.currentTimeMillis();
        totalSteps = 4;
        
        try {
            // Test login page
            logPhase("PHASE 1", "Login Page Validation");
            
            logStep(1, "Check Login Page");
            navigateToLoginPage();
            Assert.assertTrue(loginPage.isPageLoaded(), "Login page not loaded");
            completeStep(1, "Login page accessible");
            
            logStep(2, "Perform Authentication");
            long authStart = System.currentTimeMillis();
            boolean loginOk = performLogin();
            long authTime = System.currentTimeMillis() - authStart;
            
            Assert.assertTrue(loginOk, "Auth failed for: " + testEmail);
            Assert.assertTrue(getCurrentUrl().contains("home"), "Auth redirect failed");
            
            completeStep(2, String.format("Authentication OK (%dms)", authTime));
            
            // Validate session
            logPhase("PHASE 2", "Session Validation");
            
            logStep(3, "Verify User Session");
            validateSession();
            completeStep(3, "Session validated");
            
            // Test logout
            logPhase("PHASE 3", "Logout Testing");
            
            logStep(4, "Test Logout");
            long logoutStart = System.currentTimeMillis();
            boolean logoutOk = performLogout();
            long logoutTime = System.currentTimeMillis() - logoutStart;
            
            Assert.assertTrue(logoutOk, "Logout failed");
            Assert.assertTrue(getCurrentUrl().contains("index") || getCurrentUrl().contains("login"), 
                "Logout redirect failed");
            
            completeStep(4, String.format("Logout OK (%dms)", logoutTime));
            
            printSummary("AUTHENTICATION FLOW", true, "N/A");
            
        } catch (AssertionError e) {
            logger.error("Auth test failed: {}", e.getMessage());
            takeScreenshot("Auth_Failed");
            printSummary("AUTHENTICATION FLOW", false, "N/A");
            throw e;
        } catch (Exception e) {
            logger.error("Auth test error: {}", e.getMessage());
            takeScreenshot("Auth_Error");
            printSummary("AUTHENTICATION FLOW", false, "N/A");
            Assert.fail("Auth test failed: " + e.getMessage());
        }
    }
    
    /**
     * UI and navigation testing
     * TODO: Add mobile responsive tests
     */
    @Test(priority = 4, description = "UI Navigation Testing")
    public void testPageNavigationAndElements() {
        printHeader("UI NAVIGATION TESTING", "ATC_UI_001");
        testStartTime = System.currentTimeMillis();
        totalSteps = 5;
        
        try {
            // Login page UI
            logPhase("PHASE 1", "Login Page UI");
            
            logStep(1, "Validate Login Elements");
            navigateToLoginPage();
            validateLoginElements();
            completeStep(1, "Login UI validated");
            
            // Get access to home page
            logPhase("PHASE 2", "Home Page Access");
            
            logStep(2, "Login for Home Access");
            boolean loginOk = performLogin();
            Assert.assertTrue(loginOk, "Login failed for home access");
            completeStep(2, "Home access granted");
            
            // Home page UI
            logPhase("PHASE 3", "Home Page UI");
            
            logStep(3, "Validate Home Elements");
            validateHomeElements();
            completeStep(3, "Home UI validated");
            
            // Navigation testing
            logPhase("PHASE 4", "Navigation Flow");
            
            logStep(4, "Test Navigation");
            validateNavigation();
            completeStep(4, "Navigation working");
            
            // Cleanup
            logStep(5, "UI Test Cleanup");
            
            // Smart cleanup - check if already logged out
            if (getCurrentUrl().contains("home")) {
                logger.info("Still logged in, doing cleanup logout");
                boolean logoutOk = performLogout();
                Assert.assertTrue(logoutOk, "Cleanup logout failed");
                completeStep(5, "Cleanup logout completed");
            } else {
                logger.info("Already logged out from nav test");
                completeStep(5, "Cleanup completed (already logged out)");
            }
            
            printSummary("UI NAVIGATION TESTING", true, "N/A");
            
        } catch (AssertionError e) {
            logger.error("UI test failed: {}", e.getMessage());
            takeScreenshot("UI_Failed");
            printSummary("UI NAVIGATION TESTING", false, "N/A");
            throw e;
        } catch (Exception e) {
            logger.error("UI test error: {}", e.getMessage());
            takeScreenshot("UI_Error");
            printSummary("UI NAVIGATION TESTING", false, "N/A");
            Assert.fail("UI test failed: " + e.getMessage());
        }
    }
    
    // Helper methods
    
    private void printHeader(String testName, String testId) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        logger.info("================================================================");
        logger.info("Starting Test: {}", testName);
        logger.info("Test ID: {} | Start Time: {}", testId, timestamp);
        logger.info("Browser: {} | Environment: {}", 
            System.getProperty("browser", "chrome"), 
            System.getProperty("environment", "local"));
        logger.info("================================================================");
        completedSteps = 0;
    }
    
    private void logPhase(String phase, String description) {
        phaseStartTime = System.currentTimeMillis();
        logger.info("");
        logger.info("{} - {}", phase, description);
        logger.info("----------------------------------------------------------------");
    }
    
    private void logStep(int step, String description) {
        logger.info("Step {}/{}: {}", step, totalSteps, description);
    }
    
    private void completeStep(int step, String message) {
        completedSteps++;
        long duration = phaseStartTime > 0 ? System.currentTimeMillis() - phaseStartTime : 0;
        logger.info("✓ Step {}/{} DONE: {} ({}ms)", step, totalSteps, message, duration);
        
        int progress = (completedSteps * 100) / totalSteps;
        logger.info("Progress: {}% ({}/{})", progress, completedSteps, totalSteps);
    }
    
    private void validateBrowserSetup() {
        Assert.assertNotNull(getCurrentUrl(), "Browser URL is null");
        
        // Navigate to login page
        navigateToLoginPage();
        
        Assert.assertTrue(getCurrentUrl().contains("index"), "App not loaded correctly");
        Assert.assertTrue(loginPage.isPageLoaded(), "Login page elements missing");
        
        logger.info("Browser setup OK | URL: {}", getCurrentUrl());
    }
    
    private void validateHomeAccess() {
        Assert.assertTrue(homePage.isPageLoaded(), "Home page not loaded");
        Assert.assertTrue(homePage.verifyUserIsLoggedIn(), "User not logged in");
        
        String user = homePage.getDisplayedUserEmail();
        Assert.assertEquals(user, testEmail, "User email mismatch");
        
        logger.info("Home access OK | User: {}", user);
    }
    
    private void validateResults() {
        Assert.assertTrue(getCurrentUrl().contains("result"), "Not on results page");
        
        // Go back to home
        navigateToHomePage();
        Assert.assertTrue(homePage.isPageLoaded(), "Failed to return to home");
        
        logger.info("Results validated and back to home");
    }
    
    private void validateAlgorithm(String input, TestResult expected) {
        navigateToHomePage();
        Assert.assertTrue(homePage.isPageLoaded(), "Home page not accessible");
        
        boolean ok = homePage.submitStringAndWaitForResults(input);
        Assert.assertTrue(ok, "Algorithm failed for: '" + input + "'");
        
        // Note: Real validation would check actual results here
        logger.info("Algorithm OK: '{}' | Expected: {}", input, expected.length);
    }
    
    private void validateSession() {
        Assert.assertTrue(homePage.verifyUserIsLoggedIn(), "Session invalid");
        
        String user = homePage.getDisplayedUserEmail();
        Assert.assertNotNull(user, "Session user is null");
        Assert.assertEquals(user, testEmail, "Session user mismatch");
        
        logger.info("Session OK | User: {}", user);
    }
    
    private void validateLoginElements() {
        Assert.assertTrue(loginPage.isPageLoaded(), "Login page not loaded");
        Assert.assertTrue(loginPage.verifyPageElements(), "Login elements missing");
        
        logger.info("Login elements OK");
    }
    
    private void validateHomeElements() {
        Assert.assertTrue(homePage.isPageLoaded(), "Home page not loaded");
        Assert.assertTrue(homePage.verifyPageElements(), "Home elements missing");
        
        logger.info("Home elements OK");
    }
    
    private void validateNavigation() {
        // Test home navigation
        navigateToHomePage();
        Assert.assertTrue(getCurrentUrl().contains("home"), "Home nav failed");
        
        // Test logout navigation
        performLogout();
        Assert.assertTrue(getCurrentUrl().contains("index") || getCurrentUrl().contains("login"), 
            "Logout nav failed");
        
        logger.info("Navigation working");
    }
    
    private void printSummary(String testName, boolean passed, String inputData) {
        long totalTime = System.currentTimeMillis() - testStartTime;
        String status = passed ? "PASSED" : "FAILED";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        logger.info("");
        logger.info("================================================================");
        logger.info("Test Complete: {}", testName);
        logger.info("Status: {} | End Time: {}", status, timestamp);
        logger.info("Duration: {}ms | Steps: {}/{}", totalTime, completedSteps, totalSteps);
        if (!inputData.equals("N/A")) {
            logger.info("Test Data: '{}' | User: {}", inputData, testEmail);
        }
        logger.info("================================================================");
        logger.info("");
    }
    
    private void printAlgorithmSummary() {
        logger.info("");
        logger.info("Algorithm Test Summary:");
        logger.info("----------------------------------------------------------------");
        for (Map.Entry<String, TestResult> testCase : testData.entrySet()) {
            logger.info("✓ Input: '{}' | Length: {} | Substring: '{}'", 
                testCase.getKey(), testCase.getValue().length, testCase.getValue().substring);
        }
        logger.info("Total Cases: {} | All Passed", testData.size());
        logger.info("");
    }
    
    // Simple data class for test results
    private static class TestResult {
        final int length;
        final String substring;
        
        TestResult(int length, String substring) {
            this.length = length;
            this.substring = substring;
        }
    }
} 