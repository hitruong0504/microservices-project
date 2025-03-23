package com.hitruong.CloudGateway.security;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
public class OktaOauth2WebSecurity {


    private final org.springframework.cloud.client.circuitbreaker.Customizer customizer;
    @Value("${auth0.audience}")
    private String audience;


    private final ReactiveClientRegistrationRepository repository;

    public OktaOauth2WebSecurity(ReactiveClientRegistrationRepository repository, org.springframework.cloud.client.circuitbreaker.Customizer customizer) {
        this.repository = repository;
        this.customizer = customizer;
    }


    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange(authorizeExchangeSpec ->
                        authorizeExchangeSpec.anyExchange().authenticated())
                .oauth2ResourceServer(
                        oAuth2 -> oAuth2.jwt(
                                jwt -> jwt.jwtAuthenticationConverter(converter())
                        )
                );

        return http.build();
    }

    private ServerOAuth2AuthorizationRequestResolver oAuth(
            ReactiveClientRegistrationRepository repository
    ){
        DefaultServerOAuth2AuthorizationRequestResolver resolver
                = new DefaultServerOAuth2AuthorizationRequestResolver(repository);

        resolver.setAuthorizationRequestCustomizer(authBuilder());

        return resolver;
    }


    private Consumer<OAuth2AuthorizationRequest.Builder> authBuilder(){
        return customizer -> customizer
                .additionalParameters(
                        params -> params.put("audience", audience)
                );
    }


    public ReactiveJwtAuthenticationConverterAdapter converter() {

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            JwtGrantedAuthoritiesConverter defaultConverter = new JwtGrantedAuthoritiesConverter();

            Collection<GrantedAuthority> authorities = defaultConverter.convert(jwt);

            Collection<GrantedAuthority> customerAuthorities
                    = jwt.getClaimAsStringList("https://hitruong.com/roles")
                    .stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList());

            authorities.addAll(customerAuthorities);
            return authorities;
        });

        return new ReactiveJwtAuthenticationConverterAdapter(converter);
    }

}
