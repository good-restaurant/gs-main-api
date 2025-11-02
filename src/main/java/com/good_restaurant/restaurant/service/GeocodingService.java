package com.good_restaurant.restaurant.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.good_restaurant.config.GeocodingConfig;
import com.good_restaurant.restaurant.dto.GeocodeResultDto;
import java.math.RoundingMode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeocodingService {

	private static final int COORD_SCALE = 6;   // Coordinate decimal places
	private final GeocodingConfig geoConfig;

	public GeocodeResultDto geocode(String address) {
		NaverResponse res = geoConfig.geocodingWebClient().get()
				.uri(uriBuilder -> uriBuilder
						.queryParam("query", address)
						.build())
				.retrieve()
				.bodyToMono(NaverResponse.class)
				.onErrorResume(e -> Mono.empty())
				.block();

		if (res == null || res.addresses == null || res.addresses.isEmpty()) {
			log.warn("Geocoding failed for address: {}", address);
			return null;
		}

		NaverResponse.Address ad = res.addresses.get(0);
		BigDecimal lon = new BigDecimal(ad.x).setScale(COORD_SCALE, RoundingMode.HALF_UP);
		BigDecimal lat = new BigDecimal(ad.y).setScale(COORD_SCALE, RoundingMode.HALF_UP);
		return new GeocodeResultDto(lon, lat);
	}

	private static class NaverResponse {
		public List<Address> addresses;
		@JsonIgnoreProperties(ignoreUnknown = true)
		static class Address {
			public String x; // longitude
			public String y; // latitude
		}
	}
}
