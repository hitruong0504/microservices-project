package com.hitruong.OrderService;

import com.hitruong.OrderService.external.intercept.RestTemplateInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@SpringBootApplication
@EnableFeignClients
public class OrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate(RestTemplateInterceptor interceptor) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setInterceptors(List.of(interceptor));
		return restTemplate;
	}

	@Bean
	public OAuth2AuthorizedClientManager clientManager(
			ClientRegistrationRepository clientRegistrationRepository,
			OAuth2AuthorizedClientRepository authorizedClientRepository
	) {
		OAuth2AuthorizedClientProvider clientProvider
				= OAuth2AuthorizedClientProviderBuilder.builder()
				.clientCredentials()
				.build();

		DefaultOAuth2AuthorizedClientManager authorizedClientManager
				= new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientRepository);

		authorizedClientManager.setAuthorizedClientProvider(clientProvider);

		return authorizedClientManager;
	}

}
