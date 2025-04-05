package com.vault.demo;

import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;
import org.springframework.vault.authentication.TokenAuthentication;
import java.util.HashMap;
import java.util.Map;

public class ReadWriteVault {
    private static VaultTemplate vaultTemplate;

    public static VaultTemplate initServer() {
        /**
         * Initialize a vault client at http://localhost:8200 and
         * prints out whether the client is authenticated.
         */
        VaultEndpoint endpoint = VaultEndpoint.create("localhost", 8200);
        endpoint.setScheme("http");
        
        String token = System.getenv("VAULT_TOKEN");
        vaultTemplate = new VaultTemplate(endpoint, new TokenAuthentication(token));
        
        boolean isAuthenticated = token != null && !token.isEmpty();
        System.out.println(" Is client authenticated: " + isAuthenticated);
        return vaultTemplate;
    }

    public static void writeSecret(String secretPath, String key, String value) {
        /**
         * Writes key=value pair to secret/secret_path for client and 
         * prints out information about the secret written, such as
         * its creation time.
         */
        Map<String, Object> secretData = new HashMap<>();
        secretData.put(key, value);
        
        Map<String, Object> data = new HashMap<>();
        data.put("data", secretData);
        
        try {
            VaultResponse createResponse = vaultTemplate.write("secret/data/" + secretPath, data);
            System.out.println(createResponse != null ? createResponse.getData() : "No response data");
        } catch (Exception e) {
            System.err.println("Error writing secret: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void readSecret(String secretPath) {
        /**
         * Reads secrets from secret/secret_path for client and 
         * prints out information about the secret read.
         */
        try {
            VaultResponse readResponse = vaultTemplate.read("secret/data/" + secretPath);
            if (readResponse != null) {
                System.out.println(readResponse.getData());
            } else {
                System.out.println("No secret found at path: " + secretPath);
            }
        } catch (Exception e) {
            System.err.println("Error reading secret: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void deleteSecret(String secretPath) {
        /**
         * Delete secrets from secret/secret_path for client.
         */
        try {
            vaultTemplate.delete("secret/data/" + secretPath);
            System.out.println("Secret at path " + secretPath + " deleted successfully.");
        } catch (Exception e) {
            System.err.println("Error deleting secret: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        initServer(); // Initialize the client
        
        if (args.length < 2) {
            System.out.println("Insufficient arguments inputted. Please include the method name AND secret path.");
            System.exit(1);
        }
        
        String methodName = args[0];
        String path = args[1];

        if (methodName.equals("write_secret")) {
            if (args.length < 4) {
                System.out.println("Insufficient arguments inputted. Please include the secret key AND value.");
                System.exit(1);
            }
            String key = args[2];
            String value = args[3];
            writeSecret(path, key, value);
        } 
        else if (methodName.equals("read_secret")) {
            if (args.length > 2) {
                System.out.println("Too many arguments. Please only include the secret path.");
                System.exit(1);
            }
            readSecret(path);
        } 
        else if (methodName.equals("delete_secret")) {
            if (args.length > 2) {
                System.out.println("Too many arguments. Please only include the secret path.");
                System.exit(1);
            }
            deleteSecret(path);
        } 
        else {
            System.out.println("Please input one of the valid methods: write_secret OR read_secret OR delete_secret");
            System.exit(1);
        }
    }
}