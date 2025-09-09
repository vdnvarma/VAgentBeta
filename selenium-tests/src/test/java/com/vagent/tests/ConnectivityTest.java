package com.vagent.tests;

import com.vagent.config.ConfigManager;
import com.vagent.driver.DriverFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Quick connectivity test to verify URLs are accessible
 */
public class ConnectivityTest extends BaseTest {

    @Test(priority = 1, description = "Test connection to production application")
    public void testApplicationConnectivity() {
        String baseUrl = ConfigManager.getBaseUrl();
        System.out.println("Testing connection to: " + baseUrl);
        
        // Navigate to the application
        DriverFactory.getDriver().get(baseUrl);
        
        // Wait for page to load
        try {
            Thread.sleep(5000); // Give extra time for cloud app to load
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Check if page loaded successfully
        String currentUrl = DriverFactory.getDriver().getCurrentUrl();
        String pageTitle = DriverFactory.getDriver().getTitle();
        
        System.out.println("Current URL: " + currentUrl);
        System.out.println("Page Title: " + pageTitle);
        
        // Verify we can access the application
        Assert.assertNotNull(currentUrl, "Should have a current URL");
        Assert.assertNotNull(pageTitle, "Should have a page title");
        
        // Check if it's not an error page
        String pageSource = DriverFactory.getDriver().getPageSource();
        Assert.assertFalse(pageSource.contains("This site can't be reached"), 
                          "Application should be accessible");
        Assert.assertFalse(pageSource.contains("502 Bad Gateway"), 
                          "Application should not show server errors");
        Assert.assertFalse(pageSource.contains("404"), 
                          "Application should not show 404 errors");
    }

    @Test(priority = 2, description = "Test if login page elements are accessible")
    public void testLoginPageAccessibility() {
        // This test will help verify if the React app is loading properly
        String pageSource = DriverFactory.getDriver().getPageSource();
        
        // Check for common elements that should be present
        boolean hasReactRoot = pageSource.contains("root") || pageSource.contains("app");
        boolean hasVAgentContent = pageSource.toLowerCase().contains("vagent") || 
                                  pageSource.contains("login") || 
                                  pageSource.contains("email");
        
        System.out.println("Page contains React root: " + hasReactRoot);
        System.out.println("Page contains VAgent content: " + hasVAgentContent);
        
        // At minimum, we should have some indication the app is loading
        Assert.assertTrue(hasReactRoot || hasVAgentContent, 
                         "Page should contain React app or VAgent content");
    }
}
