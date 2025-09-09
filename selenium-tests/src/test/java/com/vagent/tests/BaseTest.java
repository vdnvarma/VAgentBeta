package com.vagent.tests;

import com.vagent.config.ConfigManager;
import com.vagent.driver.DriverFactory;
import com.vagent.utils.ScreenshotUtils;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Base test class with common setup and teardown
 */
public class BaseTest implements ITestListener {
    
    @BeforeMethod
    public void setUp() {
        DriverFactory.createDriver();
        
        // Handle Render cold start - try multiple times if needed
        String baseUrl = ConfigManager.getBaseUrl();
        int maxRetries = 3;
        boolean pageLoaded = false;
        
        for (int i = 0; i < maxRetries && !pageLoaded; i++) {
            try {
                DriverFactory.getDriver().get(baseUrl);
                
                // Wait for page to load completely with extended timeout
                Thread.sleep(5000); // Initial wait for Render wake-up
                
                // Check if page is responsive
                String readyState = (String) ((org.openqa.selenium.JavascriptExecutor) DriverFactory.getDriver())
                    .executeScript("return document.readyState");
                
                if ("complete".equals(readyState)) {
                    pageLoaded = true;
                } else {
                    Thread.sleep(10000); // Wait longer for slow loading
                }
                
            } catch (Exception e) {
                System.out.println("Attempt " + (i + 1) + " failed to load page: " + e.getMessage());
                if (i == maxRetries - 1) {
                    throw new RuntimeException("Failed to load application after " + maxRetries + " attempts", e);
                }
                try {
                    Thread.sleep(15000); // Wait 15 seconds before retry
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Test setup interrupted", ie);
                }
            }
        }
    }
    
    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        if (DriverFactory.getDriver() != null) {
            ScreenshotUtils.takeScreenshotOnFailure(
                DriverFactory.getDriver(), 
                result.getMethod().getMethodName()
            );
        }
    }
}
