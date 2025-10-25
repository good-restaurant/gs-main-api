package com.good_restaurant.restaurant.controller;

import com.good_restaurant.restaurant.dto.DataListResDto;
import com.good_restaurant.restaurant.dto.RestaurantCoordinateResDto;
import com.good_restaurant.restaurant.dto.RestaurantCreateReqDto;
import com.good_restaurant.restaurant.dto.RestaurantUpdateReqDto;
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
