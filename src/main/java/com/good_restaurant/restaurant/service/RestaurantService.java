package com.good_restaurant.restaurant.service;

import com.good_restaurant.restaurant.domain.Restaurant;
import com.good_restaurant.restaurant.domain.RestaurantDetail;
import com.good_restaurant.restaurant.dto.GeocodeResultDto;
import com.good_restaurant.restaurant.dto.RestaurantCoordinateResDto;
import com.good_restaurant.restaurant.dto.RestaurantCreateReqDto;
import com.good_restaurant.restaurant.dto.RestaurantDetailResDto;
import com.good_restaurant.restaurant.repository.RestaurantDetailRepository;
import com.good_restaurant.restaurant.repository.RestaurantRepository;
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
public class RestaurantService {

	private final RestaurantRepository restaurantRepository;
	private final RestaurantDetailRepository restaurantDetailRepository;
	private final GeocodingService geocodingService;

	/**
	 * 전체 음식점 좌표를 랜덤으로 조회합니다.
	 * @param limit 조회할 음식점 좌표 제한 개수
	 * @return 음식점 좌표 리스트
	 */
	@Transactional(readOnly = true)
	public List<RestaurantCoordinateResDto> getEntireRestaurantCoordinates(int limit) {
		List<RestaurantCoordinateResDto> restaurantCoordinates = restaurantRepository.pickRandom(limit)
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
		return restaurantCoordinates;
	}

	/**
	 * 도로명 주소를 기반으로 주변 음식점 목록을 조회합니다.
	 * @param address 도로명 주소
	 * @param radius 검색 반경(위/경도 단위)
	 * @param limit 조회할 음식점 개수
	 * @return 주변 음식점 리스트
	 */
	@Transactional(readOnly = true)
	public List<RestaurantDetailResDto> getNearbyRestaurants(String address, double radius, int limit) {
		// 주소로부터 위도/경도 얻기 (GeocodingService 사용 가정)
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
	 * 음식점 데이터를 생성합니다.
	 * @param dto 음식점 생성 요청 DTO
	 */
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
