package com.hitruong.OrderService.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

@Configuration
public class SecurityConfig {

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration clientRegistration = ClientRegistration
                .withRegistrationId("my-client")
                .clientId("U6eYfSJBh4qfdRg8plH0aPlNJ3rIoY5F")
                .clientSecret("TYmMRQdiSVRL86JoDne9bwRp6UHYyxyVhvF3Gxn7XAq50M1ok1K9KV9HFA5AdZKg")
                .authorizationGrantType(org.springframework.security.oauth2.core.AuthorizationGrantType.CLIENT_CREDENTIALS)
                .tokenUri("https://dev-24zg0xhmmaeh8cpi.us.auth0.com/oauth/token")
                .build();

        return new InMemoryClientRegistrationRepository(clientRegistration);
    }
}
