package com.good_restaurant.restaurant.service.impl;

import com.good_restaurant.restaurant.domain.Restaurant;
import com.good_restaurant.restaurant.domain.RestaurantDetail;
import com.good_restaurant.restaurant.dto.GeocodeResultDto;
import com.good_restaurant.restaurant.dto.RestaurantCoordinateResDto;
import com.good_restaurant.restaurant.dto.RestaurantCreateReqDto;
import com.good_restaurant.restaurant.dto.RestaurantDetailResDto;
import com.good_restaurant.restaurant.repository.RestaurantDetailRepository;
import com.good_restaurant.restaurant.repository.RestaurantRepository;
import com.good_restaurant.restaurant.service.GeocodingService;
import com.good_restaurant.restaurant.service.RestaurantService;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
/**
 * RestaurantService 구현체.
 * <p>
 * DI 설명:
 * <ul>
 *   <li>생성자 주입(@RequiredArgsConstructor)으로 Repository 들과 {@link com.good_restaurant.restaurant.service.GeocodingService} 인터페이스를 의존성으로 받습니다.</li>
 *   <li>상위 계층(컨트롤러 등)은 {@link com.good_restaurant.restaurant.service.RestaurantService} 인터페이스에만 의존합니다.</li>
 *   <li>구현체 교체(예: 캐싱 추가/외부 API 전략 변경)나 테스트 시 모킹이 용이해집니다.</li>
 * </ul>
 * 스프링 빈 등록은 {@link org.springframework.stereotype.Service} 애너테이션으로 처리됩니다.
 */
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantDetailRepository restaurantDetailRepository;
    private final GeocodingService geocodingService;
	
	/**
     * 전체 음식점 좌표를 랜덤으로 조회합니다.
     * @param limit 조회할 음식점 좌표 제한 개수
     * @return 음식점 좌표 리스트
     */
    @Override
    @Transactional(readOnly = true)
    public List<RestaurantCoordinateResDto> getEntireRestaurantCoordinates(int limit) {
        Pageable limitPage = PageRequest.of(0, limit);

        return restaurantRepository.pickRandom(limitPage)
            .stream()
            .map(restaurant -> RestaurantCoordinateResDto.builder()
                .id(restaurant.getId())
                .restaurantName(restaurant.getRestaurantName())
                .address(restaurant.getAddress())
                .category(restaurant.getCategory())
                .lon(restaurant.getLon())
                .lat(restaurant.getLat())
                .build()
            )
            .collect(Collectors.toList());
    }

    /**
     * 도로명 주소를 기반으로 주변 음식점 목록을 조회합니다.
     * @param address 도로명 주소
     * @param radius 검색 반경(위/경도 단위)
     * @param limit 조회할 음식점 개수
     * @return 주변 음식점 리스트
     */
    @Override
    @Transactional(readOnly = true)
    public List<RestaurantDetailResDto> getNearbyRestaurants(String address, double radius, int limit) {
        // 주소로부터 위도/경도 얻기
        GeocodeResultDto geoResult = geocodingService.geocode(address);

        // 위도/경도 정보를 얻지 못한 경우 빈 리스트 반환(예: 잘못된 주소)
        if (geoResult == null) {
            return Collections.emptyList();
        }

        BigDecimal minLat = geoResult.getLat().subtract(BigDecimal.valueOf(radius));
        BigDecimal maxLat = geoResult.getLat().add(BigDecimal.valueOf(radius));
        BigDecimal minLon = geoResult.getLon().subtract(BigDecimal.valueOf(radius));
        BigDecimal maxLon = geoResult.getLon().add(BigDecimal.valueOf(radius));

        Pageable limitPage = PageRequest.of(0, limit);

        return restaurantRepository.findNearbyWithDetail(
                minLat, maxLat, minLon, maxLon,
                geoResult.getLat(), geoResult.getLon(),
                limitPage
        );
    }

    /**
     * 행정동 기반으로 주변 음식점 목록을 조회합니다.
     * @param emd 행정동
     * @return 주변 음식점 리스트
     */
    @Override
    @Transactional(readOnly = true)
    public List<RestaurantDetailResDto> findRestaurantsByEmd(String emd, int limit) {
        Pageable limitPage = PageRequest.of(0, limit);
        return restaurantRepository.findRestaurantsByEmd(emd, limitPage);
    }

    /**
     * 음식점 데이터를 생성합니다.
     * @param dto 음식점 생성 요청 DTO
     */
    @Override
    @Transactional
    public void createRestaurantData(RestaurantCreateReqDto dto) {
        Restaurant newRestaurant = restaurantRepository.save(Restaurant.builder()
                .restaurantName(dto.getRestaurantName())
                .address(dto.getAddress())
                .category(dto.getCategory())
                .lon(dto.getLon())
                .lat(dto.getLat())
                .build()
        );

        restaurantDetailRepository.save(RestaurantDetail.builder()
                .restaurant(newRestaurant)
                .menu(dto.getMenu())
                .phoneNumber(dto.getPhoneNumber())
                .build()
        );
    }
}
