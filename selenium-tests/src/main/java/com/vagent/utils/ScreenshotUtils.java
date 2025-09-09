package com.vagent.utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for taking screenshots
 */
public class ScreenshotUtils {
    
    private static final String SCREENSHOT_DIR = "test-output/screenshots/";
    
    /**
     * Take screenshot and save to file
     */
    public static String takeScreenshot(WebDriver driver, String testName) {
        try {
            // Create screenshots directory if it doesn't exist
            File screenshotDir = new File(SCREENSHOT_DIR);
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }
            
            // Generate filename with timestamp
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String timestamp = dateFormat.format(new Date());
            String fileName = testName + "_" + timestamp + ".png";
            String filePath = SCREENSHOT_DIR + fileName;
            
            // Take screenshot
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File sourceFile = screenshot.getScreenshotAs(OutputType.FILE);
            File destFile = new File(filePath);
            
            // Copy screenshot to destination
            FileUtils.copyFile(sourceFile, destFile);
            
            System.out.println("Screenshot saved: " + filePath);
            return filePath;
            
        } catch (IOException e) {
            System.err.println("Failed to take screenshot: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Take screenshot on test failure
     */
    public static String takeScreenshotOnFailure(WebDriver driver, String testName) {
        return takeScreenshot(driver, testName + "_FAILED");
    }
}
