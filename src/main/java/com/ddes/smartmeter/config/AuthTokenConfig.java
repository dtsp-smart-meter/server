package com.ddes.smartmeter.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.List;

/**
 * Configuration class for loading authentication tokens from a JSON file.
 * This class reads a JSON file containing authentication tokens and makes them available
 * to the application.
 */
@Configuration
public class AuthTokenConfig {

    // Path to the JSON file containing authentication tokens, loaded as a Spring resource.
    @Value("classpath:authenticationTokens.json")
    private Resource authTokensFile;

    // List to hold the authentication tokens loaded from the JSON file.
    private List<String> authTokens;

    /**
     * Method executed after the bean is initialized.
     * It reads the JSON file, parses it, and populates the authTokens list.
     */
    @PostConstruct
    public void init() throws IOException {
        // Create an ObjectMapper to read and parse JSON files.
        ObjectMapper objectMapper = new ObjectMapper();

        // Parse the JSON file into an instance of AuthenticationTokenData.
        AuthenticationTokenData data = objectMapper.readValue(authTokensFile.getInputStream(), AuthenticationTokenData.class);

        // Extract the authentication tokens and store them in the authTokens list.
        this.authTokens = data.getAuthenticationTokens();
    }

    /**
     * Getter method to retrieve the list of authentication tokens.
     */
    public List<String> getAuthTokens() {
        return authTokens;
    }

    /**
     * Private inner class representing the structure of the JSON file.
     * The JSON file is expected to have a single field `authenticationTokens`
     * which is a list of strings.
     */
    private static class AuthenticationTokenData {
        // Field matching the JSON file structure to hold authentication tokens.
        private List<String> authenticationTokens;

        // Getter method for the authentication tokens.
        public List<String> getAuthenticationTokens() {
            return authenticationTokens;
        }
    }
}