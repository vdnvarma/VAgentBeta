package com.vagent.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.By;
import java.util.List;

/**
 * Page Object Model for Home page
 */
public class HomePage extends BasePage {

    // Page elements
    @FindBy(css = "h1")
    private WebElement pageTitle;

    @FindBy(xpath = "//button[contains(text(), 'Logout')]")
    private WebElement logoutButton;

    @FindBy(css = "button:contains('New Project')")
    private WebElement newProjectButton;

    @FindBy(css = "input[type='text']")
    private WebElement projectNameInput;

    @FindBy(css = "button[type='submit']")
    private WebElement createProjectSubmitButton;

    @FindBy(css = "button:contains('Cancel')")
    private WebElement cancelButton;

    @FindBy(css = ".project")
    private List<WebElement> projectCards;

    @FindBy(css = ".fixed.inset-0")
    private WebElement modal;

    @FindBy(css = "label:contains('Project Name')")
    private WebElement projectNameLabel;

    @FindBy(css = "button:contains('Create')")
    private WebElement createButton;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    /**
     * Check if home page is displayed
     */
    public boolean isHomePageDisplayed() {
        try {
            // Wait for page to load completely first
            waitForPageToLoad();
            
            // Check current URL contains the expected path
            String currentUrl = getCurrentUrl();
            if (!currentUrl.contains("vagent.onrender.com") || currentUrl.contains("/login") || currentUrl.contains("/register")) {
                return false;
            }
            
            // Look for the VAgent header or any specific home page element
            try {
                waitForElementToBeVisible(pageTitle);
                return pageTitle.getText().contains("VAgent");
            } catch (Exception e) {
                // If title not found, check for other home page indicators
                try {
                    WebElement newProjectBtn = driver.findElement(By.xpath("//button[contains(text(), 'New Project')]"));
                    return newProjectBtn.isDisplayed();
                } catch (Exception ex) {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Click logout button
     */
    public LoginPage logout() {
        clickElement(logoutButton);
        return new LoginPage(driver);
    }

    /**
     * Click new project button
     */
    public HomePage clickNewProjectButton() {
        // Find and click the new project button using a more specific selector
        WebElement newProjectBtn = driver.findElement(By.xpath("//button[contains(text(), 'New Project')]"));
        clickElement(newProjectBtn);
        return this;
    }

    /**
     * Enter project name in modal
     */
    public HomePage enterProjectName(String projectName) {
        waitForElementToBeVisible(projectNameInput);
        sendTextToElement(projectNameInput, projectName);
        return this;
    }

    /**
     * Click create button in modal
     */
    public HomePage clickCreateButton() {
        WebElement createBtn = driver.findElement(By.xpath("//button[contains(text(), 'Create')]"));
        clickElement(createBtn);
        return this;
    }

    /**
     * Click cancel button in modal
     */
    public HomePage clickCancelButton() {
        clickElement(cancelButton);
        return this;
    }

    /**
     * Create a new project
     */
    public HomePage createProject(String projectName) {
        clickNewProjectButton();
        enterProjectName(projectName);
        clickCreateButton();
        return this;
    }

    /**
     * Get number of projects
     */
    public int getProjectCount() {
        return projectCards.size();
    }

    /**
     * Check if modal is displayed
     */
    public boolean isModalDisplayed() {
        try {
            waitForElementToBeVisible(modal);
            return modal.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if project exists by name
     */
    public boolean isProjectExists(String projectName) {
        try {
            WebElement project = driver.findElement(By.xpath("//h2[contains(text(), '" + projectName + "')]"));
            return project.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Open project by name
     */
    public ProjectPage openProject(String projectName) {
        WebElement project = driver.findElement(By.xpath("//h2[contains(text(), '" + projectName + "')]/following-sibling::div//button[contains(@class, 'open-btn')]"));
        clickElement(project);
        return new ProjectPage(driver);
    }

    /**
     * Edit project by name
     */
    public HomePage editProject(String projectName) {
        WebElement editBtn = driver.findElement(By.xpath("//h2[contains(text(), '" + projectName + "')]/following-sibling::div//button[contains(@class, 'edit-btn')]"));
        clickElement(editBtn);
        return this;
    }

    /**
     * Delete project by name
     */
    public HomePage deleteProject(String projectName) {
        WebElement deleteBtn = driver.findElement(By.xpath("//h2[contains(text(), '" + projectName + "')]/following-sibling::div//button[contains(@class, 'delete-btn')]"));
        clickElement(deleteBtn);
        // Handle confirmation dialog
        driver.switchTo().alert().accept();
        return this;
    }

    /**
     * Get collaborators count for a project
     */
    public int getCollaboratorsCount(String projectName) {
        WebElement collaboratorsSpan = driver.findElement(By.xpath("//h2[contains(text(), '" + projectName + "')]/following-sibling::div//span[@class='font-bold text-purple-500']"));
        return Integer.parseInt(collaboratorsSpan.getText());
    }

    /**
     * Check if logout button is displayed
     */
    public boolean isLogoutButtonDisplayed() {
        try {
            return logoutButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
