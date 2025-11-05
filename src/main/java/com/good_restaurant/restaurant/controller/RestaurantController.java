package com.good_restaurant.restaurant.controller;

import com.good_restaurant.restaurant.dto.*;
import com.good_restaurant.restaurant.service.RestaurantService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurant")
public class RestaurantController {

	private final RestaurantService restaurantService;

	/**
	 * 전체 음식점 좌표를 랜덤으로 조회합니다.
	 * @param limit 조회할 음식점 좌표 개수(기본값: 100)
	 * @return 음식점 좌표 리스트
	 */
	@GetMapping("/all")
	public DataListResDto<RestaurantCoordinateResDto> getEntireRestaurantCoordinates(
			@RequestParam(defaultValue = "100") int limit) {
		List<RestaurantCoordinateResDto> data = restaurantService.getEntireRestaurantCoordinates(limit);
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
	public DataListResDto<RestaurantDetailResDto> getNearbyRestaurants(
			@RequestParam String address,
			@RequestParam(defaultValue = "0.1") double radius,
			@RequestParam(defaultValue = "20") int limit) {
		List<RestaurantDetailResDto> data =
				restaurantService.getNearbyRestaurants(address, radius, limit);
		return new DataListResDto<>(data);
	}

	/**
	 * 행정동 기반으로 주변 음식점 목록을 조회합니다.
	 * @param emd 행정동
	 * @return 주변 음식점 리스트
	 */
	@GetMapping("/emd")
	public DataListResDto<RestaurantDetailResDto> findRestaurantsByEmd(
			@RequestParam String emd,
			@RequestParam(defaultValue = "20") int limit) {
		List<RestaurantDetailResDto> data =
				restaurantService.findRestaurantsByEmd(emd, limit);
		return new DataListResDto<>(data);
	}

	/**
	 * 음식점 데이터를 생성합니다.
	 * @param dto 음식점 생성 요청 DTO
	 */
	@PostMapping("/create")
	public void createRestaurantData(@RequestBody RestaurantDto dto) {
		restaurantService.createRestaurantData(dto);
	}

	/**
	 * 음식점 데이터를 수정합니다.
	 * @param dto 음식점 수정 요청 DTO
	 */
	@PatchMapping("/update")
	public void updateRestaurantData(@RequestBody RestaurantDto dto) {
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
