package com.good_restaurant.restaurant.service.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.good_restaurant.config.GeocodingConfig;
import com.good_restaurant.restaurant.dto.GeocodeResultDto;
import com.good_restaurant.restaurant.service.GeocodingService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
/**
 * GeocodingService 구현체.
 * <p>
 * DI 설명:
 * <ul>
 *   <li>생성자 주입(@RequiredArgsConstructor)으로 {@link com.good_restaurant.config.GeocodingConfig} 를 주입받습니다.</li>
 *   <li>상위 계층(컨트롤러 등)은 {@link com.good_restaurant.restaurant.service.GeocodingService} 인터페이스에만 의존합니다.</li>
 *   <li>이 구조는 외부 지오코딩 공급자 교체나 테스트 시 모킹/대체 구현 등록을 유연하게 해줍니다.</li>
 * </ul>
 * 스프링 빈 등록은 {@link org.springframework.stereotype.Service} 애너테이션으로 처리됩니다.
 */
public class GeocodingServiceImpl implements GeocodingService {

    private static final int COORD_SCALE = 6;   // Coordinate decimal places
    private final GeocodingConfig geoConfig;
	

    @Override
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
