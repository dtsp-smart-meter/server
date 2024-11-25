package com.ddes.smartmeter.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.List;

@Configuration
public class AuthTokenConfig {

    @Value("classpath:authenticationTokens.json")
    private Resource authTokensFile;

    private List<String> authTokens;

    @PostConstruct
    public void init() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        AuthenticationTokenData data = objectMapper.readValue(authTokensFile.getInputStream(), AuthenticationTokenData.class);
        this.authTokens = data.getAuthenticationTokens();
    }

    public List<String> getAuthTokens() {
        return authTokens;
    }

    private static class AuthenticationTokenData {
        private List<String> authenticationTokens;

        public List<String> getAuthenticationTokens() {
            return authenticationTokens;
        }
    }
}