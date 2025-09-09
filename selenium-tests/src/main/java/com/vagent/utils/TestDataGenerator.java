package com.vagent.utils;

import java.util.Random;

/**
 * Utility class for generating test data
 */
public class TestDataGenerator {
    
    private static final Random random = new Random();
    
    /**
     * Generate random email address
     */
    public static String generateRandomEmail() {
        String[] domains = {"gmail.com", "yahoo.com", "outlook.com", "test.com"};
        String randomString = generateRandomString(8);
        String domain = domains[random.nextInt(domains.length)];
        return "test" + randomString + "@" + domain;
    }
    
    /**
     * Generate random password
     */
    public static String generateRandomPassword() {
        return "Pass" + generateRandomString(6) + random.nextInt(100);
    }
    
    /**
     * Generate random project name
     */
    public static String generateRandomProjectName() {
        String[] prefixes = {"Project", "Task", "Work", "Demo", "Test"};
        String prefix = prefixes[random.nextInt(prefixes.length)];
        return prefix + "_" + generateRandomString(5);
    }
    
    /**
     * Generate random string
     */
    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < length; i++) {
            result.append(characters.charAt(random.nextInt(characters.length())));
        }
        
        return result.toString();
    }
    
    /**
     * Generate random number within range
     */
    public static int generateRandomNumber(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }
    
    /**
     * Generate test message
     */
    public static String generateTestMessage() {
        String[] messages = {
            "Hello team, how is everyone doing?",
            "Let's work on this project together",
            "Can we review the latest changes?",
            "Great progress on the development!",
            "Looking forward to the next milestone"
        };
        return messages[random.nextInt(messages.length)];
    }
}
