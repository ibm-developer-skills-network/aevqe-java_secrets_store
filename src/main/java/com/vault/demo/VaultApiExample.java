package com.vault.demo;

import com.vault.demo.config.VaultConfig;
import com.vault.demo.service.VaultService;
import com.vault.demo.util.VaultUtil;
import org.springframework.vault.support.VaultResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Example application demonstrating the usage of Spring Vault Java API.
 * This class provides examples of how to use the API for various Vault operations.
 */
public class VaultApiExample {

    public static void main(String[] args) {
        System.out.println("Spring Vault Java API Example");
        System.out.println("============================");

        // Check if Vault token is set
        String token = System.getenv("VAULT_TOKEN");
        if (token == null || token.isEmpty()) {
            System.err.println("Error: VAULT_TOKEN environment variable is not set");
            System.err.println("Please set it using: export VAULT_TOKEN=your-vault-token");
            System.exit(1);
        }

        // Example 1: Basic initialization
        basicInitialization();

        // Example 2: Writing and reading a single secret
        singleSecretExample();

        // Example 3: Writing and reading multiple secrets
        multipleSecretsExample();

        // Example 4: Deleting secrets
        deleteSecretExample();

        // Example 5: Custom Vault configuration
        customConfigExample();

        System.out.println("\nAll examples completed successfully!");
    }

    /**
     * Example 1: Basic initialization of the Vault service
     */
    private static void basicInitialization() {
        System.out.println("\nExample 1: Basic Initialization");
        System.out.println("-------------------------------");

        // Create a VaultService with default configuration
        VaultService vaultService = new VaultService();
        
        // Check if authenticated
        boolean isAuthenticated = vaultService.isAuthenticated();
        System.out.println("Is authenticated: " + isAuthenticated);
        
        if (!isAuthenticated) {
            System.err.println("Authentication failed. Please check your VAULT_TOKEN.");
            System.exit(1);
        }
    }

    /**
     * Example 2: Writing and reading a single secret
     */
    private static void singleSecretExample() {
        System.out.println("\nExample 2: Single Secret");
        System.out.println("------------------------");

        VaultService vaultService = new VaultService();
        String path = "example/single";
        String key = "username";
        String value = "admin";

        try {
            // Write a secret
            System.out.println("Writing secret: " + key + "=" + value + " to path: " + path);
            VaultResponse writeResponse = vaultService.writeSecret(path, key, value);
            System.out.println("Write response: " + writeResponse.getData());

            // Read the secret back
            System.out.println("\nReading secret from path: " + path);
            VaultResponse readResponse = vaultService.readSecret(path);
            
            // Print formatted secret data
            VaultUtil.printFormattedSecretData(readResponse, vaultService);
            
        } catch (Exception e) {
            System.err.println("Error in single secret example: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Example 3: Writing and reading multiple secrets
     */
    private static void multipleSecretsExample() {
        System.out.println("\nExample 3: Multiple Secrets");
        System.out.println("---------------------------");

        VaultService vaultService = new VaultService();
        String path = "example/multiple";

        try {
            // Create a map of secrets
            Map<String, Object> secrets = new HashMap<>();
            secrets.put("username", "admin");
            secrets.put("password", "secret123");
            secrets.put("api_key", "abcd1234");
            secrets.put("environment", "development");

            // Write multiple secrets
            System.out.println("Writing multiple secrets to path: " + path);
            VaultResponse writeResponse = vaultService.writeSecrets(path, secrets);
            System.out.println("Write response: " + writeResponse.getData());

            // Read the secrets back
            System.out.println("\nReading secrets from path: " + path);
            VaultResponse readResponse = vaultService.readSecret(path);
            
            // Print formatted secret data
            VaultUtil.printFormattedSecretData(readResponse, vaultService);
            
        } catch (Exception e) {
            System.err.println("Error in multiple secrets example: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Example 4: Deleting secrets
     */
    private static void deleteSecretExample() {
        System.out.println("\nExample 4: Deleting Secrets");
        System.out.println("--------------------------");

        VaultService vaultService = new VaultService();
        String path = "example/to-delete";

        try {
            // First, write a secret
            System.out.println("Writing a test secret to path: " + path);
            Map<String, Object> secrets = new HashMap<>();
            secrets.put("test_key", "test_value");
            vaultService.writeSecrets(path, secrets);

            // Verify it exists
            System.out.println("Verifying secret exists at path: " + path);
            VaultResponse readResponse = vaultService.readSecret(path);
            if (readResponse != null) {
                System.out.println("Secret exists. Data: " + vaultService.extractSecretData(readResponse));
            }

            // Delete the secret
            System.out.println("Deleting secret at path: " + path);
            vaultService.deleteSecret(path);
            System.out.println("Secret deleted successfully");

            // Verify it's gone
            System.out.println("Verifying secret is deleted from path: " + path);
            try {
                VaultResponse verifyResponse = vaultService.readSecret(path);
                if (verifyResponse == null || vaultService.extractSecretData(verifyResponse) == null) {
                    System.out.println("Secret successfully deleted");
                } else {
                    System.out.println("Secret still exists (this might happen if using Vault with versioning)");
                }
            } catch (Exception e) {
                System.out.println("Secret successfully deleted (verified by exception: " + e.getMessage() + ")");
            }
            
        } catch (Exception e) {
            System.err.println("Error in delete secret example: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Example 5: Using custom Vault configuration
     */
    private static void customConfigExample() {
        System.out.println("\nExample 5: Custom Vault Configuration");
        System.out.println("------------------------------------");

        try {
            // Create a custom Vault configuration
            String host = "localhost";  // Change this for your Vault server
            int port = 8200;            // Change this for your Vault server
            String scheme = "http";     // Use "https" for production
            String token = System.getenv("VAULT_TOKEN");
            
            System.out.println("Creating custom Vault configuration:");
            System.out.println("Host: " + host);
            System.out.println("Port: " + port);
            System.out.println("Scheme: " + scheme);
            
            VaultConfig customConfig = new VaultConfig(host, port, scheme, token);
            VaultService customVaultService = new VaultService(customConfig);
            
            // Check if authenticated with custom config
            boolean isAuthenticated = customVaultService.isAuthenticated();
            System.out.println("Is authenticated with custom config: " + isAuthenticated);
            
            if (isAuthenticated) {
                // Write a test secret with custom config
                String path = "example/custom-config";
                Map<String, Object> secrets = new HashMap<>();
                secrets.put("custom_key", "custom_value");
                
                System.out.println("Writing secret with custom config to path: " + path);
                customVaultService.writeSecrets(path, secrets);
                
                // Read it back
                System.out.println("Reading secret with custom config from path: " + path);
                VaultResponse readResponse = customVaultService.readSecret(path);
                VaultUtil.printFormattedSecretData(readResponse, customVaultService);
            }
            
        } catch (Exception e) {
            System.err.println("Error in custom config example: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
