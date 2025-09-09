package com.vagent.tests;

import com.vagent.driver.DriverFactory;
import com.vagent.pages.LoginPage;
import com.vagent.pages.HomePage;
import com.vagent.pages.RegisterPage;
import com.vagent.utils.TestDataGenerator;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test class for project management functionality
 */
public class ProjectManagementTest extends BaseTest {

    private String testEmail;
    private String testPassword;
    private HomePage homePage;

    @Test(priority = 1, description = "Setup test user and login")
    public void setupTestUser() {
        testEmail = TestDataGenerator.generateRandomEmail();
        testPassword = TestDataGenerator.generateRandomPassword();
        
        LoginPage loginPage = new LoginPage(DriverFactory.getDriver());
        RegisterPage registerPage = loginPage.clickRegisterLink();
        homePage = registerPage.register(testEmail, testPassword);
        
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Should be on home page after registration");
    }

    @Test(priority = 2, description = "Verify home page elements", dependsOnMethods = {"setupTestUser"})
    public void testHomePageElements() {
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Home page should be displayed");
        Assert.assertTrue(homePage.isLogoutButtonDisplayed(), "Logout button should be displayed");
    }

    @Test(priority = 3, description = "Verify create new project modal", dependsOnMethods = {"setupTestUser"})
    public void testCreateProjectModal() {
        homePage.clickNewProjectButton();
        
        Assert.assertTrue(homePage.isModalDisplayed(), "Create project modal should be displayed");
        
        // Close modal
        homePage.clickCancelButton();
    }

    @Test(priority = 4, description = "Create a new project", dependsOnMethods = {"setupTestUser"})
    public void testCreateNewProject() {
        String projectName = TestDataGenerator.generateRandomProjectName();
        int initialProjectCount = homePage.getProjectCount();
        
        homePage.createProject(projectName);
        
        // Wait a moment for the project to be created
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        Assert.assertTrue(homePage.isProjectExists(projectName), "Project should be created and visible");
    }

    @Test(priority = 5, description = "Verify project details", dependsOnMethods = {"testCreateNewProject"})
    public void testProjectDetails() {
        String projectName = TestDataGenerator.generateRandomProjectName();
        homePage.createProject(projectName);
        
        // Wait for project creation
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        Assert.assertTrue(homePage.isProjectExists(projectName), "Project should exist");
        
        // Check collaborators count (should be 1 - the creator)
        int collaboratorsCount = homePage.getCollaboratorsCount(projectName);
        Assert.assertEquals(collaboratorsCount, 1, "Project should have 1 collaborator (the creator)");
    }

    @Test(priority = 6, description = "Edit project name", dependsOnMethods = {"setupTestUser"})
    public void testEditProject() {
        String originalProjectName = TestDataGenerator.generateRandomProjectName();
        String newProjectName = TestDataGenerator.generateRandomProjectName();
        
        // Create project
        homePage.createProject(originalProjectName);
        
        // Wait for project creation
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Edit project
        homePage.editProject(originalProjectName);
        
        Assert.assertTrue(homePage.isModalDisplayed(), "Edit project modal should be displayed");
        
        // Enter new name and save
        homePage.enterProjectName(newProjectName);
        homePage.clickCreateButton(); // This button text changes to "Update" in edit mode
        
        // Wait for update
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        Assert.assertTrue(homePage.isProjectExists(newProjectName), "Project should be updated with new name");
    }

    @Test(priority = 7, description = "Delete project", dependsOnMethods = {"setupTestUser"})
    public void testDeleteProject() {
        String projectName = TestDataGenerator.generateRandomProjectName();
        
        // Create project
        homePage.createProject(projectName);
        
        // Wait for project creation
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        Assert.assertTrue(homePage.isProjectExists(projectName), "Project should exist before deletion");
        
        // Delete project
        homePage.deleteProject(projectName);
        
        // Wait for deletion
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        Assert.assertFalse(homePage.isProjectExists(projectName), "Project should be deleted");
    }

    @Test(priority = 8, description = "Create multiple projects", dependsOnMethods = {"setupTestUser"})
    public void testCreateMultipleProjects() {
        int initialCount = homePage.getProjectCount();
        
        // Create 3 projects
        for (int i = 0; i < 3; i++) {
            String projectName = "Project_" + i + "_" + TestDataGenerator.generateRandomString(4);
            homePage.createProject(projectName);
            
            // Wait between creations
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        // Verify project count increased
        int finalCount = homePage.getProjectCount();
        Assert.assertTrue(finalCount > initialCount, "Project count should increase after creating multiple projects");
    }

    @Test(priority = 9, description = "Verify project creation with empty name", dependsOnMethods = {"setupTestUser"})
    public void testCreateProjectWithEmptyName() {
        homePage.clickNewProjectButton();
        
        // Try to create project with empty name
        homePage.enterProjectName("");
        homePage.clickCreateButton();
        
        // Modal should still be displayed as validation should prevent creation
        Assert.assertTrue(homePage.isModalDisplayed(), "Modal should still be displayed with empty project name");
        
        // Close modal
        homePage.clickCancelButton();
    }

    @Test(priority = 10, description = "Verify project creation with special characters", dependsOnMethods = {"setupTestUser"})
    public void testCreateProjectWithSpecialCharacters() {
        String projectName = "Test@Project#123!";
        
        homePage.createProject(projectName);
        
        // Wait for project creation
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        Assert.assertTrue(homePage.isProjectExists(projectName), "Project with special characters should be created");
    }
}
