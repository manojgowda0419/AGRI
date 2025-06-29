package config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

/**
 * DriverManager - Manages WebDriver lifecycle and configuration
 * Handles browser initialization, configuration, and cleanup
 */
public class DriverManager {
    private static final Logger logger = LogManager.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    
    /**
     * Initialize WebDriver based on browser configuration
     * @return WebDriver instance
     */
    public static WebDriver initializeDriver() {
        String browserName = ConfigManager.getBrowser().toLowerCase();
        WebDriver driver = null;
        
        logger.info("Initializing {} driver", browserName);
        
        try {
            switch (browserName) {
                case "chrome":
                    driver = initializeChromeDriver();
                    break;
                case "firefox":
                    driver = initializeFirefoxDriver();
                    break;
                case "edge":
                    driver = initializeEdgeDriver();
                    break;
                default:
                    logger.error("Unsupported browser: {}", browserName);
                    throw new IllegalArgumentException("Unsupported browser: " + browserName);
            }
            
            configureDriver(driver);
            driverThreadLocal.set(driver);
            logger.info("{} driver initialized successfully", browserName);
            
        } catch (Exception e) {
            logger.error("Failed to initialize {} driver: {}", browserName, e.getMessage());
            throw new RuntimeException("Driver initialization failed", e);
        }
        
        return driver;
    }
    
    /**
     * Initialize Chrome driver with options
     * @return ChromeDriver instance
     */
    private static WebDriver initializeChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        
        // Add Chrome-specific options
        options.addArguments("--disable-web-security");
        options.addArguments("--disable-features=VizDisplayCompositor");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        
        if (ConfigManager.isHeadless()) {
            options.addArguments("--headless");
            logger.info("Running Chrome in headless mode");
        }
        
        return new ChromeDriver(options);
    }
    
    /**
     * Initialize Firefox driver with options
     * @return FirefoxDriver instance
     */
    private static WebDriver initializeFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        
        if (ConfigManager.isHeadless()) {
            options.addArguments("--headless");
            logger.info("Running Firefox in headless mode");
        }
        
        return new FirefoxDriver(options);
    }
    
    /**
     * Initialize Edge driver with options
     * @return EdgeDriver instance
     */
    private static WebDriver initializeEdgeDriver() {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        
        if (ConfigManager.isHeadless()) {
            options.addArguments("--headless");
            logger.info("Running Edge in headless mode");
        }
        
        return new EdgeDriver(options);
    }
    
    /**
     * Configure driver with timeouts and window settings
     * @param driver WebDriver instance to configure
     */
    private static void configureDriver(WebDriver driver) {
        // Set timeouts
        driver.manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(ConfigManager.getImplicitWait()));
        driver.manage().timeouts()
                .pageLoadTimeout(Duration.ofSeconds(ConfigManager.getPageLoadTimeout()));
        
        // Set window size
        if (ConfigManager.shouldMaximize()) {
            driver.manage().window().maximize();
            logger.info("Browser window maximized");
        } else {
            Dimension dimension = new Dimension(
                    ConfigManager.getWindowWidth(), 
                    ConfigManager.getWindowHeight()
            );
            driver.manage().window().setSize(dimension);
            logger.info("Browser window set to {}x{}", 
                    ConfigManager.getWindowWidth(), 
                    ConfigManager.getWindowHeight());
        }
    }
    
    /**
     * Get current WebDriver instance for this thread
     * @return WebDriver instance
     */
    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
            logger.warn("Driver not initialized for current thread, initializing new driver");
            driver = initializeDriver();
        }
        return driver;
    }
    
    /**
     * Quit driver and clean up resources
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
                logger.info("Driver quit successfully");
            } catch (Exception e) {
                logger.error("Error while quitting driver: {}", e.getMessage());
            } finally {
                driverThreadLocal.remove();
            }
        }
    }
    
    /**
     * Check if driver is initialized
     * @return true if driver is initialized
     */
    public static boolean isDriverInitialized() {
        return driverThreadLocal.get() != null;
    }
    
    /**
     * Navigate to URL
     * @param url URL to navigate to
     */
    public static void navigateToUrl(String url) {
        WebDriver driver = getDriver();
        logger.info("Navigating to URL: {}", url);
        driver.get(url);
        logger.info("Successfully navigated to: {}", url);
    }
    
    /**
     * Get current URL
     * @return Current URL
     */
    public static String getCurrentUrl() {
        return getDriver().getCurrentUrl();
    }
    
    /**
     * Get page title
     * @return Page title
     */
    public static String getPageTitle() {
        return getDriver().getTitle();
    }
} 