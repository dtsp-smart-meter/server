package com.ddes.smartmeter.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.List;

@Configuration
public class AuthenticationTokenConfig {

        @Value("classpath:authentication_tokens.json")
        private Resource jsonFile;

        private List<String> items;

        @PostConstruct
        public void init() throws IOException {
            ObjectMapper objectMapper = new ObjectMapper();
            AuthenticationTokenData configData = objectMapper.readValue(jsonFile.getInputStream(), AuthenticationTokenData.class);
            this.items = configData.getAuthenticationTokens();
        }

        public List<String> getItems() {
            return items;
        }

        private static class AuthenticationTokenData {
            private List<String> authenticationTokens;

            public List<String> getAuthenticationTokens() {
                return authenticationTokens;
            }

            public void setAuthenticationTokens(List<String> authenticationTokens) {
                this.authenticationTokens = authenticationTokens;
            }
        }
}
