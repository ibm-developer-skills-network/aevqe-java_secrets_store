# Spring Vault Java API

A Java application for managing secrets in HashiCorp Vault using Spring Vault. This API provides functionality to read, write, and delete key-value secrets in Vault.

## Prerequisites

- JDK 21
- Maven
- HashiCorp Vault server running (default: http://localhost:8200)
- Vault token set as environment variable `VAULT_TOKEN`

## Features

- Initialize connection to Vault server
- Write key-value secrets to Vault
- Read secrets from Vault
- Delete secrets from Vault

## Building the Project

```bash
mvn clean package
```

This will create a JAR file with dependencies in the `target` directory.

## Usage

The application can be run from the command line with the following syntax:

```bash
# Set your Vault token as an environment variable
export VAULT_TOKEN=your-vault-token

# Run the application
java -jar target/vault-secrets-store-1.0-SNAPSHOT-jar-with-dependencies.jar <command> <path> [key] [value]
```

### Available Commands

1. **Write a secret**:
   ```bash
   java -jar target/vault-secrets-store-1.0-SNAPSHOT-jar-with-dependencies.jar write_secret my/secret/path myKey myValue
   ```

2. **Read a secret**:
   ```bash
   java -jar target/vault-secrets-store-1.0-SNAPSHOT-jar-with-dependencies.jar read_secret my/secret/path
   ```

3. **Delete a secret**:
   ```bash
   java -jar target/vault-secrets-store-1.0-SNAPSHOT-jar-with-dependencies.jar delete_secret my/secret/path
   ```

## Notes

- This application uses Spring Vault to interact with HashiCorp Vault
- Secrets are stored in the KV v2 secrets engine (path: `secret/data/`)
- Token authentication is used for simplicity