package com.good_restaurant.restaurant.service;

import com.good_restaurant.restaurant.domain.Restaurant;
import com.good_restaurant.restaurant.domain.RestaurantDetail;
import com.good_restaurant.restaurant.dto.RestaurantCoordinateResDto;
import com.good_restaurant.restaurant.dto.RestaurantCreateReqDto;
import com.good_restaurant.restaurant.repository.RestaurantDetailRepository;
import com.good_restaurant.restaurant.repository.RestaurantRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RestaurantService {

	private final RestaurantRepository restaurantRepository;
	private final RestaurantDetailRepository restaurantDetailRepository;

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
	 * 음식점에 대한 자세한 데이터를 조회합니다.
	 * @param limit
	 * @return
	 */

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
