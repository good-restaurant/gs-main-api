package com.good_restaurant.restaurant.controller;

import com.good_restaurant.restaurant.domain.Restaurant;
import com.good_restaurant.restaurant.dto.*;
import com.good_restaurant.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/restaurant")
public class RestaurantControllerV2 {

	private final RestaurantService restaurantService;

	/**
	 * 전체 음식점 좌표를 랜덤으로 조회합니다.
	 * @param limit 조회할 음식점 좌표 개수(기본값: 100)
	 * @return 음식점 좌표 리스트
	 */
	@GetMapping("/all")
	public DataListResDto<RestaurantCoordinateResDto> getEntireRestaurantCoordinates(
			@RequestParam(defaultValue = "100") int limit) {
		List<RestaurantCoordinateResDto> data = restaurantService.getEntireRestaurantCoordinatesQueryDsl(limit);
		return new DataListResDto<>(data);
	}

	/**
	 * 도로명 주소를 기반으로 주변 음식점 목록을 조회합니다.
	 * @param address 도로명 주소
	 * @param radius 검색 반경(위/경도 단위, 기본값: 0.1)
	 * @param limit 조회할 음식점 개수(기본값: 20)
	 * @return 주변 음식점 리스트
	 */
	@GetMapping("/nearby")
	public List<RestaurantDetailResDto> getNearbyRestaurants(
			@RequestParam String address,
			@RequestParam(defaultValue = "0.1") double radius,
			@RequestParam(defaultValue = "20") int limit) {
		List<Restaurant> data =
				restaurantService.getNearbyRestaurantsQueryDsl(address, radius, limit);
		return restaurantService.;
	}

	/**
	 * 행정동 기반으로 주변 음식점 목록을 조회합니다.
	 * @param emd 행정동
	 * @return 주변 음식점 리스트
	 */
	@GetMapping("/emd")
	public List<Restaurant> findRestaurantsByEmd(
			@RequestParam String emd,
			@RequestParam(defaultValue = "20") int limit) {
		return restaurantService.findRestaurantsByEmdQueryDsl(emd, limit);
	}

	/**
	 * 음식점 데이터를 생성합니다.
	 * @param dto 음식점 생성 요청 DTO
	 */
	@PostMapping("/create")
	public void createRestaurantData(@RequestBody RestaurantCreateReqDto dto) {
		restaurantService.createRestaurantData(dto);
	}

	/**
	 * 음식점 데이터를 수정합니다.
	 * @param dto 음식점 수정 요청 DTO
	 */
	@PatchMapping("/update")
	public void updateRestaurantData(@RequestBody RestaurantUpdateReqDto dto) {
		// TODO: Implementation and decide request/response DTOs
	}

	/**
	 * 음식점 데이터를 삭제합니다.
	 * @param restaurantId 삭제할 음식점 ID
	 */
	@DeleteMapping("/delete")
	public void deleteRetaurantData(@RequestParam Long restaurantId) {
		// TODO: Implementation and decide request/response DTOs
	}
}
