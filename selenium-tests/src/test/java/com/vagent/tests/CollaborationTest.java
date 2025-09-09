package com.vagent.tests;

import com.vagent.driver.DriverFactory;
import com.vagent.pages.LoginPage;
import com.vagent.pages.HomePage;
import com.vagent.pages.ProjectPage;
import com.vagent.pages.RegisterPage;
import com.vagent.utils.TestDataGenerator;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test class for collaboration functionality
 */
public class CollaborationTest extends BaseTest {

    private String creatorEmail;
    private String creatorPassword;
    private String collaboratorEmail;
    private String collaboratorPassword;
    private String projectName;
    private HomePage homePage;
    private ProjectPage projectPage;

    @Test(priority = 1, description = "Setup test users")
    public void setupTestUsers() {
        // Create creator account
        creatorEmail = TestDataGenerator.generateRandomEmail();
        creatorPassword = TestDataGenerator.generateRandomPassword();
        
        LoginPage loginPage = new LoginPage(DriverFactory.getDriver());
        RegisterPage registerPage = loginPage.clickRegisterLink();
        homePage = registerPage.register(creatorEmail, creatorPassword);
        
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Creator should be on home page");
        
        // Create a project
        projectName = TestDataGenerator.generateRandomProjectName();
        homePage.createProject(projectName);
        
        // Wait for project creation
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        Assert.assertTrue(homePage.isProjectExists(projectName), "Project should be created");
        
        // Logout creator
        loginPage = homePage.logout();
        
        // Create collaborator account
        collaboratorEmail = TestDataGenerator.generateRandomEmail();
        collaboratorPassword = TestDataGenerator.generateRandomPassword();
        
        registerPage = loginPage.clickRegisterLink();
        homePage = registerPage.register(collaboratorEmail, collaboratorPassword);
        
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Collaborator should be on home page");
        
        // Logout collaborator
        loginPage = homePage.logout();
        
        // Login back as creator
        homePage = loginPage.login(creatorEmail, creatorPassword);
    }

    @Test(priority = 2, description = "Open project and verify project page elements", dependsOnMethods = {"setupTestUsers"})
    public void testProjectPageElements() {
        projectPage = homePage.openProject(projectName);
        
        Assert.assertTrue(projectPage.isProjectPageDisplayed(), "Project page should be displayed");
        Assert.assertTrue(projectPage.isMessageInputDisplayed(), "Message input should be displayed");
        Assert.assertTrue(projectPage.isCodeEditorDisplayed(), "Code editor should be displayed");
    }

    @Test(priority = 3, description = "Send message in project", dependsOnMethods = {"testProjectPageElements"})
    public void testSendMessage() {
        String testMessage = TestDataGenerator.generateTestMessage();
        int initialMessageCount = projectPage.getMessageCount();
        
        projectPage.sendMessage(testMessage);
        
        // Wait for message to be sent
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        int finalMessageCount = projectPage.getMessageCount();
        Assert.assertTrue(finalMessageCount > initialMessageCount, "Message count should increase after sending message");
    }

    @Test(priority = 4, description = "Toggle collaborators panel", dependsOnMethods = {"testProjectPageElements"})
    public void testToggleCollaboratorsPanel() {
        // Initially panel should be closed
        Assert.assertFalse(projectPage.isSidePanelOpen(), "Side panel should be closed initially");
        
        // Toggle panel
        projectPage.toggleCollaboratorsPanel();
        
        // Wait for animation
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        Assert.assertTrue(projectPage.isSidePanelOpen(), "Side panel should be open after toggle");
        
        // Toggle again to close
        projectPage.toggleCollaboratorsPanel();
        
        // Wait for animation
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        Assert.assertFalse(projectPage.isSidePanelOpen(), "Side panel should be closed after second toggle");
    }

    @Test(priority = 5, description = "Check initial collaborators count", dependsOnMethods = {"testProjectPageElements"})
    public void testInitialCollaboratorsCount() {
        projectPage.toggleCollaboratorsPanel();
        
        // Wait for panel to open
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        int collaboratorCount = projectPage.getCollaboratorCount();
        Assert.assertEquals(collaboratorCount, 1, "Project should have 1 collaborator initially (creator)");
        
        // Close panel
        projectPage.toggleCollaboratorsPanel();
    }

    @Test(priority = 6, description = "Navigate back to home page", dependsOnMethods = {"testProjectPageElements"})
    public void testNavigateBackToHome() {
        homePage = projectPage.goBack();
        
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Should navigate back to home page");
        Assert.assertTrue(homePage.isProjectExists(projectName), "Project should still exist on home page");
    }

    @Test(priority = 7, description = "Test multiple message sending", dependsOnMethods = {"setupTestUsers"})
    public void testMultipleMessages() {
        projectPage = homePage.openProject(projectName);
        int initialMessageCount = projectPage.getMessageCount();
        
        // Send multiple messages
        for (int i = 0; i < 3; i++) {
            String message = "Test message " + (i + 1) + ": " + TestDataGenerator.generateTestMessage();
            projectPage.sendMessage(message);
            
            // Wait between messages
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        int finalMessageCount = projectPage.getMessageCount();
        Assert.assertTrue(finalMessageCount >= initialMessageCount + 3, "Should have at least 3 more messages");
    }

    @Test(priority = 8, description = "Test project persistence across sessions", dependsOnMethods = {"setupTestUsers"})
    public void testProjectPersistence() {
        // Go back to home
        homePage = projectPage.goBack();
        
        // Logout and login again
        LoginPage loginPage = homePage.logout();
        homePage = loginPage.login(creatorEmail, creatorPassword);
        
        // Verify project still exists
        Assert.assertTrue(homePage.isProjectExists(projectName), "Project should persist across sessions");
        
        // Open project again
        projectPage = homePage.openProject(projectName);
        Assert.assertTrue(projectPage.isProjectPageDisplayed(), "Should be able to open project after re-login");
    }

    @Test(priority = 9, description = "Test message persistence", dependsOnMethods = {"testMultipleMessages"})
    public void testMessagePersistence() {
        // Messages should persist when reopening project
        int messageCount = projectPage.getMessageCount();
        Assert.assertTrue(messageCount > 0, "Messages should persist when reopening project");
    }

    @Test(priority = 10, description = "Test empty message handling", dependsOnMethods = {"testProjectPageElements"})
    public void testEmptyMessage() {
        int initialMessageCount = projectPage.getMessageCount();
        
        // Try to send empty message
        projectPage.sendMessage("");
        
        // Wait a moment
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        int finalMessageCount = projectPage.getMessageCount();
        Assert.assertEquals(finalMessageCount, initialMessageCount, "Empty message should not be sent");
    }
}
