package com.vagent.tests;

import com.vagent.driver.DriverFactory;
import com.vagent.pages.LoginPage;
import com.vagent.pages.HomePage;
import com.vagent.pages.RegisterPage;
import com.vagent.utils.TestDataGenerator;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test class for UI elements and responsiveness
 */
public class UIElementsTest extends BaseTest {

    private String testEmail;
    private String testPassword;
    private HomePage homePage;

    @Test(priority = 1, description = "Setup test user")
    public void setupTestUser() {
        testEmail = TestDataGenerator.generateRandomEmail();
        testPassword = TestDataGenerator.generateRandomPassword();
        
        LoginPage loginPage = new LoginPage(DriverFactory.getDriver());
        RegisterPage registerPage = loginPage.clickRegisterLink();
        homePage = registerPage.register(testEmail, testPassword);
        
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Should be on home page");
    }

    @Test(priority = 2, description = "Verify page layout and styling", dependsOnMethods = {"setupTestUser"})
    public void testPageLayout() {
        // Check if main elements are visible
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Home page should be displayed");
        Assert.assertTrue(homePage.isLogoutButtonDisplayed(), "Logout button should be visible");
        
        // Verify page title
        String pageTitle = homePage.getPageTitle();
        Assert.assertNotNull(pageTitle, "Page should have a title");
    }

    @Test(priority = 3, description = "Test responsive design elements", dependsOnMethods = {"setupTestUser"})
    public void testResponsiveElements() {
        // Test window resizing (simulate mobile view)
        DriverFactory.getDriver().manage().window().setSize(new org.openqa.selenium.Dimension(768, 1024));
        
        // Wait for layout adjustment
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Elements should still be visible
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Page should be responsive on smaller screens");
        
        // Restore window size
        DriverFactory.getDriver().manage().window().maximize();
    }

    @Test(priority = 4, description = "Test button interactions", dependsOnMethods = {"setupTestUser"})
    public void testButtonInteractions() {
        // Test new project button
        homePage.clickNewProjectButton();
        Assert.assertTrue(homePage.isModalDisplayed(), "Modal should appear when clicking new project button");
        
        // Test cancel button
        homePage.clickCancelButton();
        
        // Wait for modal to close
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        Assert.assertFalse(homePage.isModalDisplayed(), "Modal should close when clicking cancel");
    }

    @Test(priority = 5, description = "Test form validation", dependsOnMethods = {"setupTestUser"})
    public void testFormValidation() {
        // Open new project modal
        homePage.clickNewProjectButton();
        
        // Try to submit with empty project name
        homePage.enterProjectName("");
        homePage.clickCreateButton();
        
        // Modal should remain open (form validation should prevent submission)
        Assert.assertTrue(homePage.isModalDisplayed(), "Modal should remain open with invalid input");
        
        // Close modal
        homePage.clickCancelButton();
    }

    @Test(priority = 6, description = "Test navigation elements", dependsOnMethods = {"setupTestUser"})
    public void testNavigationElements() {
        // Create a project to test navigation
        String projectName = TestDataGenerator.generateRandomProjectName();
        homePage.createProject(projectName);
        
        // Wait for project creation
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Open project
        var projectPage = homePage.openProject(projectName);
        Assert.assertTrue(projectPage.isProjectPageDisplayed(), "Should navigate to project page");
        
        // Navigate back
        homePage = projectPage.goBack();
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Should navigate back to home page");
    }

    @Test(priority = 7, description = "Test visual elements", dependsOnMethods = {"setupTestUser"})
    public void testVisualElements() {
        // Check if page has proper styling
        String currentUrl = homePage.getCurrentUrl();
        Assert.assertNotNull(currentUrl, "Page should have a valid URL");
        
        // Verify logout button is positioned correctly
        Assert.assertTrue(homePage.isLogoutButtonDisplayed(), "Logout button should be visible");
    }

    @Test(priority = 8, description = "Test modal behavior", dependsOnMethods = {"setupTestUser"})
    public void testModalBehavior() {
        // Test modal opening
        homePage.clickNewProjectButton();
        Assert.assertTrue(homePage.isModalDisplayed(), "Modal should open");
        
        // Test modal with valid input
        String projectName = TestDataGenerator.generateRandomProjectName();
        homePage.enterProjectName(projectName);
        homePage.clickCreateButton();
        
        // Wait for project creation
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Modal should close and project should be created
        Assert.assertFalse(homePage.isModalDisplayed(), "Modal should close after successful creation");
        Assert.assertTrue(homePage.isProjectExists(projectName), "Project should be created");
    }

    @Test(priority = 9, description = "Test page refresh behavior", dependsOnMethods = {"setupTestUser"})
    public void testPageRefresh() {
        // Create a project
        String projectName = TestDataGenerator.generateRandomProjectName();
        homePage.createProject(projectName);
        
        // Wait for project creation
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Refresh page
        DriverFactory.getDriver().navigate().refresh();
        
        // Wait for page to load
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Re-initialize page object after refresh
        homePage = new HomePage(DriverFactory.getDriver());
        
        // Project should still be visible after refresh
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Page should still be accessible after refresh");
        Assert.assertTrue(homePage.isProjectExists(projectName), "Project should persist after page refresh");
    }

    @Test(priority = 10, description = "Test browser navigation", dependsOnMethods = {"setupTestUser"})
    public void testBrowserNavigation() {
        String initialUrl = DriverFactory.getDriver().getCurrentUrl();
        
        // Create and open a project
        String projectName = TestDataGenerator.generateRandomProjectName();
        homePage.createProject(projectName);
        
        // Wait for project creation
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        var projectPage = homePage.openProject(projectName);
        String projectUrl = DriverFactory.getDriver().getCurrentUrl();
        
        // URLs should be different
        Assert.assertNotEquals(initialUrl, projectUrl, "URL should change when navigating to project");
        
        // Use browser back button
        DriverFactory.getDriver().navigate().back();
        
        // Should be back on home page
        String backUrl = DriverFactory.getDriver().getCurrentUrl();
        Assert.assertEquals(initialUrl, backUrl, "Should return to home page URL with browser back");
    }
}
