package core;

import java.io.*;
import java.nio.file.*;

public class LoginManager {
    private static final String PIN_FILE = "db/pin.txt";
    private static final int MAX_ATTEMPTS = 3;
    private static int attemptCount = 0;
    
    // Initialize default PIN on first run
    static {
        initializeDefaultPin();
    }
    
    // Load the stored PIN from the file
    public static String loadPin() {
        try {
            if (!Files.exists(Paths.get(PIN_FILE))) {
                return null;
            }
            return Files.readString(Paths.get(PIN_FILE)).trim();
        } catch (IOException e) {
            System.err.println("Error reading PIN file: " + e.getMessage());
            return null;
        }
    }
    
    // Validate the entered PIN against the stored PIN
    public static boolean validatePin(String inputPin) {
        // Check attempt limit
        if (attemptCount >= MAX_ATTEMPTS) {
            System.err.println("Too many failed attempts. Please restart the application.");
            return false;
        }
        
        // Validate input
        if (inputPin == null || inputPin.trim().isEmpty()) {
            attemptCount++;
            return false;
        }
        
        // Check PIN length (4-6 digits)
        if (!inputPin.matches("\\d{4,6}")) {
            attemptCount++;
            System.err.println("PIN must be 4-6 digits only.");
            return false;
        }
        
        String storedPin = loadPin();
        if (storedPin == null) {
            attemptCount++;
            return false;
        }
        
        boolean isValid = storedPin.equals(inputPin);
        
        if (isValid) {
            attemptCount = 0; // Reset on successful login
        } else {
            attemptCount++;
        }
        
        return isValid;
    }
    
    // Change the current PIN
    public static boolean changePin(String oldPin, String newPin) {
        // Validate old PIN first
        String storedPin = loadPin();
        if (storedPin == null || !storedPin.equals(oldPin)) {
            return false;
        }
        
        // Validate new PIN format
        if (newPin == null || !newPin.matches("\\d{4,6}")) {
            System.err.println("New PIN must be 4-6 digits only.");
            return false;
        }
        
        // Don't allow same PIN
        if (oldPin.equals(newPin)) {
            System.err.println("New PIN must be different from current PIN.");
            return false;
        }
        
        try {
            // Create db directory if it doesn't exist
            Files.createDirectories(Paths.get("db"));
            
            // Write new PIN
            Files.writeString(Paths.get(PIN_FILE), newPin);
            
            System.out.println("PIN changed successfully.");
            return true;
        } catch (IOException e) {
            System.err.println("Error writing PIN file: " + e.getMessage());
            return false;
        }
    }
    
    // First-time setup or reset
    public static void initializeDefaultPin() {
        File pinFile = new File(PIN_FILE);
        
        if (!pinFile.exists()) {
            try {
                // Create db directory
                Files.createDirectories(Paths.get("db"));
                
                // Write default PIN
                Files.writeString(pinFile.toPath(), "1234");
                
                System.out.println("PIN system initialized with default PIN (1234).");
                System.out.println("Please change the default PIN after first login.");
            } catch (IOException e) {
                System.err.println("Error initializing PIN file: " + e.getMessage());
            }
        }
    }
    
    // Reset attempt counter (for testing)
    public static void resetAttempts() {
        attemptCount = 0;
    }
    
    // Get remaining attempts
    public static int getRemainingAttempts() {
        return Math.max(0, MAX_ATTEMPTS - attemptCount);
    }
    
    // Check if PIN file exists (for setup detection)
    public static boolean isPinSetup() {
        return Files.exists(Paths.get(PIN_FILE));
    }
    
    // Force reset PIN (emergency function)
    public static boolean resetToDefault() {
        try {
            Files.deleteIfExists(Paths.get(PIN_FILE));
            initializeDefaultPin();
            resetAttempts();
            System.out.println("PIN reset to default (1234).");
            return true;
        } catch (IOException e) {
            System.err.println("Error resetting PIN: " + e.getMessage());
            return false;
        }
    }
}