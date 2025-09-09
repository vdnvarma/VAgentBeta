package com.vagent.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.By;
import java.util.List;

/**
 * Page Object Model for Project page
 */
public class ProjectPage extends BasePage {

    // Page elements
    @FindBy(css = "h1")
    private WebElement pageTitle;

    @FindBy(css = ".back-button")
    private WebElement backButton;

    @FindBy(css = "button:contains('Add collaborator')")
    private WebElement addCollaboratorButton;

    @FindBy(css = ".leave-project-btn")
    private WebElement leaveProjectButton;

    @FindBy(css = "button[class*='ri-group-fill']")
    private WebElement collaboratorsToggleButton;

    @FindBy(css = ".message-box")
    private WebElement messageBox;

    @FindBy(css = "input[placeholder='Enter message']")
    private WebElement messageInput;

    @FindBy(css = "button[class*='ri-send-plane-fill']")
    private WebElement sendMessageButton;

    @FindBy(css = ".sidePanel")
    private WebElement sidePanel;

    @FindBy(css = ".file-tree")
    private WebElement fileTree;

    @FindBy(css = ".code-editor")
    private WebElement codeEditor;

    @FindBy(css = ".message")
    private List<WebElement> messages;

    @FindBy(css = ".user")
    private List<WebElement> collaborators;

    @FindBy(css = ".tree-element")
    private List<WebElement> files;

    public ProjectPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Check if project page is displayed
     */
    public boolean isProjectPageDisplayed() {
        try {
            waitForElementToBeVisible(pageTitle);
            return pageTitle.getText().contains("VAgent");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Click back button to go to home page
     */
    public HomePage goBack() {
        clickElement(backButton);
        return new HomePage(driver);
    }

    /**
     * Click add collaborator button
     */
    public ProjectPage clickAddCollaborator() {
        clickElement(addCollaboratorButton);
        return this;
    }

    /**
     * Toggle collaborators side panel
     */
    public ProjectPage toggleCollaboratorsPanel() {
        clickElement(collaboratorsToggleButton);
        return this;
    }

    /**
     * Send a message
     */
    public ProjectPage sendMessage(String message) {
        sendTextToElement(messageInput, message);
        clickElement(sendMessageButton);
        return this;
    }

    /**
     * Check if side panel is open
     */
    public boolean isSidePanelOpen() {
        try {
            return sidePanel.isDisplayed() && 
                   !sidePanel.getAttribute("class").contains("-translate-x-full");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get number of messages
     */
    public int getMessageCount() {
        return messages.size();
    }

    /**
     * Get number of collaborators
     */
    public int getCollaboratorCount() {
        return collaborators.size();
    }

    /**
     * Get number of files
     */
    public int getFileCount() {
        return files.size();
    }

    /**
     * Check if file exists
     */
    public boolean isFileExists(String fileName) {
        try {
            WebElement file = driver.findElement(By.xpath("//p[contains(text(), '" + fileName + "')]"));
            return file.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Open file by name
     */
    public ProjectPage openFile(String fileName) {
        WebElement file = driver.findElement(By.xpath("//p[contains(text(), '" + fileName + "')]/ancestor::button"));
        clickElement(file);
        return this;
    }

    /**
     * Check if code editor is displayed
     */
    public boolean isCodeEditorDisplayed() {
        try {
            waitForElementToBeVisible(codeEditor);
            return codeEditor.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Add collaborator by email
     */
    public ProjectPage addCollaborator(String email) {
        clickAddCollaborator();
        
        // Select user from the modal
        WebElement user = driver.findElement(By.xpath("//h1[contains(text(), '" + email + "')]/ancestor::div[@class='user']"));
        clickElement(user);
        
        // Click add collaborators button
        WebElement addButton = driver.findElement(By.xpath("//button[contains(text(), 'Add Collaborators')]"));
        clickElement(addButton);
        
        return this;
    }

    /**
     * Remove collaborator
     */
    public ProjectPage removeCollaborator(String email) {
        toggleCollaboratorsPanel();
        
        WebElement removeButton = driver.findElement(By.xpath(
            "//h1[contains(text(), '" + email + "')]/ancestor::div[contains(@class, 'user')]//button[contains(@class, 'remove-user')]"
        ));
        clickElement(removeButton);
        
        return this;
    }

    /**
     * Leave project
     */
    public HomePage leaveProject() {
        clickElement(leaveProjectButton);
        // Handle confirmation dialog
        driver.switchTo().alert().accept();
        return new HomePage(driver);
    }

    /**
     * Check if leave project button is displayed
     */
    public boolean isLeaveProjectButtonDisplayed() {
        try {
            return leaveProjectButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get last message text
     */
    public String getLastMessage() {
        if (!messages.isEmpty()) {
            WebElement lastMessage = messages.get(messages.size() - 1);
            return lastMessage.findElement(By.cssSelector("p")).getText();
        }
        return "";
    }

    /**
     * Check if message input is displayed
     */
    public boolean isMessageInputDisplayed() {
        try {
            waitForElementToBeVisible(messageInput);
            return messageInput.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
