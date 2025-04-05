package com.vault.demo.service;

import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;

import com.vault.demo.config.VaultConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Service class for interacting with HashiCorp Vault.
 * Provides methods for reading, writing, and deleting secrets.
 */
public class VaultService {
    private final VaultTemplate vaultTemplate;
    private final VaultConfig vaultConfig;
    
    /**
     * Constructor with default Vault configuration
     */
    public VaultService() {
        this.vaultConfig = new VaultConfig();
        this.vaultTemplate = vaultConfig.vaultTemplate();
    }
    
    /**
     * Constructor with custom Vault configuration
     * 
     * @param vaultConfig Custom Vault configuration
     */
    public VaultService(VaultConfig vaultConfig) {
        this.vaultConfig = vaultConfig;
        this.vaultTemplate = vaultConfig.vaultTemplate();
    }
    
    /**
     * Checks if the service is authenticated to Vault
     * 
     * @return true if authenticated
     */
    public boolean isAuthenticated() {
        return vaultConfig.isAuthenticated();
    }
    
    /**
     * Writes a secret to Vault
     * 
     * @param secretPath Path where the secret will be stored
     * @param key Secret key
     * @param value Secret value
     * @return VaultResponse containing metadata about the write operation
     * @throws Exception if write operation fails
     */
    public VaultResponse writeSecret(String secretPath, String key, String value) throws Exception {
        Map<String, Object> secretData = new HashMap<>();
        secretData.put(key, value);
        
        Map<String, Object> data = new HashMap<>();
        data.put("data", secretData);
        
        return vaultTemplate.write("secret/data/" + secretPath, data);
    }
    
    /**
     * Writes multiple secrets to Vault at once
     * 
     * @param secretPath Path where the secrets will be stored
     * @param secrets Map of secret key-value pairs
     * @return VaultResponse containing metadata about the write operation
     * @throws Exception if write operation fails
     */
    public VaultResponse writeSecrets(String secretPath, Map<String, Object> secrets) throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("data", secrets);
        
        return vaultTemplate.write("secret/data/" + secretPath, data);
    }
    
    /**
     * Reads a secret from Vault
     * 
     * @param secretPath Path where the secret is stored
     * @return VaultResponse containing the secret data
     * @throws Exception if read operation fails
     */
    public VaultResponse readSecret(String secretPath) throws Exception {
        return vaultTemplate.read("secret/data/" + secretPath);
    }
    
    /**
     * Deletes a secret from Vault
     * 
     * @param secretPath Path where the secret is stored
     * @throws Exception if delete operation fails
     */
    public void deleteSecret(String secretPath) throws Exception {
        vaultTemplate.delete("secret/data/" + secretPath);
    }
    
    /**
     * Extracts the actual secret data from a Vault response
     * 
     * @param response VaultResponse from a read operation
     * @return Map containing the secret data, or null if no data exists
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> extractSecretData(VaultResponse response) {
        if (response == null || response.getData() == null) {
            return null;
        }
        
        Map<String, Object> responseData = response.getData();
        if (responseData.containsKey("data")) {
            return (Map<String, Object>) responseData.get("data");
        }
        
        return null;
    }
}
