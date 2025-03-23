package com.hitruong.CloudGateway.controller;


import com.hitruong.CloudGateway.model.AuthenticationResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/authenticate")
@Log4j2
public class AuthenticationController {

    private final ReactiveOAuth2AuthorizedClientService authorizedClientService;

    public AuthenticationController(ReactiveOAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

//    @GetMapping("/login")
//    public ResponseEntity<AuthenticationResponse> login(
//            @AuthenticationPrincipal OidcUser oidcUser,
//            Model model,
//            @RegisteredOAuth2AuthorizedClient("okta") OAuth2AuthorizedClient client
//    ){
//        if (oidcUser == null || client == null) {
//            return ResponseEntity.badRequest().body(null);
//        }
//
//        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
//                .userId(oidcUser.getEmail())
//                .accessToken(client.getAccessToken().getTokenValue())
//                .refreshToken(client.getRefreshToken() != null ? client.getRefreshToken().getTokenValue() : null)
//                .expiresAt(client.getAccessToken().getExpiresAt() != null ? client.getAccessToken().getExpiresAt().getEpochSecond() : 0)
//                .authorityList(
//                        oidcUser.getAuthorities()
//                                .stream()
//                                .map(GrantedAuthority::getAuthority)
//                                .collect(Collectors.toList())
//                )
//                .build();
//
//        log.info(authenticationResponse.toString());
//        return ResponseEntity.ok(authenticationResponse);
//    }

    @GetMapping("/print-token")
    public Mono<String> getToken(Principal principal) {
        return authorizedClientService.loadAuthorizedClient("auth0", principal.getName())
                .map(
                        oAuth2AuthorizedClient -> {
                            OAuth2AccessToken accessToken
                                    = oAuth2AuthorizedClient.getAccessToken();

                            return accessToken.getTokenValue();
                        }).defaultIfEmpty("No Access Token Can be found");
    }

}
