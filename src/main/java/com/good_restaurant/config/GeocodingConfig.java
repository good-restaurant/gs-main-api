package com.good_restaurant.config;

import java.net.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Configuration
public class GeocodingConfig {

	@Value("${naver.geocode.base-url}")
	private String baseUrl;

	@Value("${naver.geocode.client-id}")
	private String apiKeyId;

	@Value("${naver.geocode.client-secret}")
	private String apiKey;

	@Bean
	public WebClient geocodingWebClient() {
		log.info("Geocoding WebClient configured with base URL: {}", baseUrl);
		log.info("Using API Key ID: {}", apiKeyId);
		return WebClient.builder()
				.baseUrl(baseUrl)
				.defaultHeaders(h -> {
					h.add("X-NCP-APIGW-API-KEY-ID", apiKeyId);
					h.add("X-NCP-APIGW-API-KEY", apiKey);
					h.add("Accept", "application/json");
				})
				.build();
	}
}
