package com.vault.demo.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.springframework.vault.support.VaultResponse;

import com.vault.demo.service.VaultService;

/**
 * Utility class for Vault operations.
 * Provides helper methods for common Vault tasks.
 */
public class VaultUtil {
    
    /**
     * Parses a string representation of key-value pairs into a Map
     * Format: "key1=value1,key2=value2"
     * 
     * @param kvString String containing key-value pairs
     * @return Map of key-value pairs
     */
    public static Map<String, Object> parseKeyValueString(String kvString) {
        Map<String, Object> result = new HashMap<>();
        
        if (kvString == null || kvString.trim().isEmpty()) {
            return result;
        }
        
        String[] pairs = kvString.split(",");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                result.put(keyValue[0].trim(), keyValue[1].trim());
            }
        }
        
        return result;
    }
    
    /**
     * Interactively prompts the user to enter multiple key-value pairs
     * 
     * @return Map of key-value pairs entered by the user
     */
    public static Map<String, Object> promptForSecrets() {
        Map<String, Object> secrets = new HashMap<>();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Enter secrets (key=value format, empty line to finish):");
        
        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine().trim();
            
            if (line.isEmpty()) {
                break;
            }
            
            String[] keyValue = line.split("=", 2);
            if (keyValue.length == 2) {
                secrets.put(keyValue[0].trim(), keyValue[1].trim());
                System.out.println("Added secret: " + keyValue[0].trim());
            } else {
                System.out.println("Invalid format. Use key=value");
            }
        }
        
        return secrets;
    }
    
    /**
     * Prints formatted secret data from a VaultResponse
     * 
     * @param response VaultResponse containing secret data
     * @param vaultService VaultService to extract data
     */
    public static void printFormattedSecretData(VaultResponse response, VaultService vaultService) {
        if (response == null) {
            System.out.println("No response received from Vault");
            return;
        }
        
        Map<String, Object> secretData = vaultService.extractSecretData(response);
        
        if (secretData == null || secretData.isEmpty()) {
            System.out.println("No secret data found in response");
            return;
        }
        
        System.out.println("\n=== Secret Data ===");
        for (Map.Entry<String, Object> entry : secretData.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        System.out.println("==================\n");
    }
    
    /**
     * Validates a secret path
     * 
     * @param path Secret path to validate
     * @return true if the path is valid
     */
    public static boolean isValidPath(String path) {
        if (path == null || path.trim().isEmpty()) {
            return false;
        }
        
        // Check for invalid characters
        if (path.contains("..") || path.contains("//")) {
            return false;
        }
        
        // Path should not start or end with a slash
        if (path.startsWith("/") || path.endsWith("/")) {
            return false;
        }
        
        return true;
    }
}
