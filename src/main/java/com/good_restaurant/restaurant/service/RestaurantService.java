package com.good_restaurant.restaurant.service;

import com.good_restaurant.restaurant.domain.Restaurant;
import com.good_restaurant.restaurant.dto.RestaurantCoordinateResDto;
import com.good_restaurant.restaurant.dto.RestaurantCreateReqDto;
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

	/**
	 * 전체 음식점 좌표를 랜덤으로 조회합니다.
	 * @param limit
	 * @return
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
	 * 음식점 데이터를 생성합니다.
	 * @param dto
	 */
	@Transactional
	public void createRestaurantData(RestaurantCreateReqDto dto) {
		restaurantRepository.save(Restaurant.builder()
			.restaurantName(dto.getRestaurantName())
			.address(dto.getAddress())
			.category(dto.getCategory())
			.menu(dto.getMenu())
			.phoneNumber(dto.getPhoneNumber())
			.lon(dto.getLon())
			.lat(dto.getLat())
			.build()
		);
	}
}
