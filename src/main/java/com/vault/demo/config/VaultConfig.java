package com.vault.demo.config;

import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.core.VaultTemplate;

/**
 * Configuration class for HashiCorp Vault connection.
 * This class provides methods to create and configure a VaultTemplate.
 */
public class VaultConfig {
    private final String host;
    private final int port;
    private final String scheme;
    private final String token;

    /**
     * Constructor with default Vault settings (localhost:8200, http)
     */
    public VaultConfig() {
        this("localhost", 8200, "http", System.getenv("VAULT_TOKEN"));
    }

    /**
     * Constructor with custom Vault settings
     * 
     * @param host   Vault server host
     * @param port   Vault server port
     * @param scheme Connection scheme (http/https)
     * @param token  Authentication token
     */
    public VaultConfig(String host, int port, String scheme, String token) {
        this.host = host;
        this.port = port;
        this.scheme = scheme;
        this.token = token;
    }

    /**
     * Creates a configured VaultTemplate
     * 
     * @return VaultTemplate instance
     */
    public VaultTemplate vaultTemplate() {
        VaultEndpoint endpoint = VaultEndpoint.create(host, port);
        endpoint.setScheme(scheme);
        
        return new VaultTemplate(endpoint, new TokenAuthentication(token));
    }

    /**
     * Checks if the configuration has a valid token
     * 
     * @return true if token is not null or empty
     */
    public boolean isAuthenticated() {
        return token != null && !token.isEmpty();
    }
}
