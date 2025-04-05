package com.vault.demo;

import com.vault.demo.service.VaultService;
import org.springframework.vault.support.VaultResponse;

import java.util.Map;

/**
 * Main class for the Spring Vault Java API application.
 * Provides command-line interface for reading, writing, and deleting secrets in Vault.
 */
public class ReadWriteVault {
    private static VaultService vaultService;

    /**
     * Initializes the Vault service
     * 
     * @return Initialized VaultService
     */
    public static VaultService initServer() {
        vaultService = new VaultService();
        System.out.println("Is client authenticated: " + vaultService.isAuthenticated());
        return vaultService;
    }

    /**
     * Writes a key-value secret to Vault
     * 
     * @param secretPath Path where the secret will be stored
     * @param key Secret key
     * @param value Secret value
     */
    public static void writeSecret(String secretPath, String key, String value) {
        try {
            VaultResponse createResponse = vaultService.writeSecret(secretPath, key, value);
            System.out.println(createResponse != null ? createResponse.getData() : "No response data");
        } catch (Exception e) {
            System.err.println("Error writing secret: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Reads a secret from Vault
     * 
     * @param secretPath Path where the secret is stored
     */
    public static void readSecret(String secretPath) {
        try {
            VaultResponse readResponse = vaultService.readSecret(secretPath);
            if (readResponse != null) {
                Map<String, Object> secretData = vaultService.extractSecretData(readResponse);
                if (secretData != null) {
                    System.out.println("Secret data: " + secretData);
                } else {
                    System.out.println("No secret data found at path: " + secretPath);
                }
                System.out.println("Full response: " + readResponse.getData());
            } else {
                System.out.println("No secret found at path: " + secretPath);
            }
        } catch (Exception e) {
            System.err.println("Error reading secret: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Deletes a secret from Vault
     * 
     * @param secretPath Path where the secret is stored
     */
    public static void deleteSecret(String secretPath) {
        try {
            vaultService.deleteSecret(secretPath);
            System.out.println("Secret at path " + secretPath + " deleted successfully.");
        } catch (Exception e) {
            System.err.println("Error deleting secret: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Main method for command-line interface
     * 
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        initServer(); // Initialize the client
        
        if (args.length < 2) {
            System.out.println("Insufficient arguments inputted. Please include the method name AND secret path.");
            printUsage();
            System.exit(1);
        }
        
        String methodName = args[0];
        String path = args[1];

        if (methodName.equals("write_secret")) {
            if (args.length < 4) {
                System.out.println("Insufficient arguments inputted. Please include the secret key AND value.");
                printUsage();
                System.exit(1);
            }
            String key = args[2];
            String value = args[3];
            writeSecret(path, key, value);
        } 
        else if (methodName.equals("read_secret")) {
            if (args.length > 2) {
                System.out.println("Too many arguments. Please only include the secret path.");
                printUsage();
                System.exit(1);
            }
            readSecret(path);
        } 
        else if (methodName.equals("delete_secret")) {
            if (args.length > 2) {
                System.out.println("Too many arguments. Please only include the secret path.");
                printUsage();
                System.exit(1);
            }
            deleteSecret(path);
        } 
        else {
            System.out.println("Please input one of the valid methods: write_secret OR read_secret OR delete_secret");
            printUsage();
            System.exit(1);
        }
    }
    
    /**
     * Prints usage information
     */
    private static void printUsage() {
        System.out.println("\nUsage:");
        System.out.println("  write_secret <path> <key> <value>  - Write a secret to Vault");
        System.out.println("  read_secret <path>                - Read a secret from Vault");
        System.out.println("  delete_secret <path>              - Delete a secret from Vault");
        System.out.println("\nExample:");
        System.out.println("  java -jar vault-secrets-store.jar write_secret my/secret/path myKey myValue");
    }
}
