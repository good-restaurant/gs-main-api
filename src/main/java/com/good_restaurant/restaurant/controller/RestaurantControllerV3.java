package com.good_restaurant.restaurant.controller;

import com.good_restaurant.restaurant.domain.Restaurant;
import com.good_restaurant.restaurant.dto.RestaurantCoordinateResDto;
import com.good_restaurant.restaurant.dto.RestaurantDetailResDto;
import com.good_restaurant.restaurant.dto.RestaurantDto;
import com.good_restaurant.restaurant.dto.RestaurantFullDto;
import com.good_restaurant.restaurant.mapper.RestaurantMapper;
import com.good_restaurant.restaurant.service.A_Exception.MergePropertyException;
import com.good_restaurant.restaurant.service.RestaurantServiceV3;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/restaurant")
public class RestaurantControllerV3 {

	private final RestaurantServiceV3 serviceV3;
	private final RestaurantMapper mapper;

	/**
	 * 전체 음식점 좌표를 랜덤으로 조회합니다.
	 * @param limit 조회할 음식점 좌표 개수(기본값: 100)
	 * @return 음식점 좌표 리스트
	 */
	@GetMapping("/all")
	public ResponseEntity<List<RestaurantCoordinateResDto>> getEntireRestaurantCoordinates(
			@RequestParam(defaultValue = "100") Integer limit) {
		List<Restaurant> restaurantList = serviceV3.randomLimit(limit);
		
		return ResponseEntity.ok(mapper.toDto3(restaurantList));
	}

	/**
	 * 도로명 주소를 기반으로 주변 음식점 목록을 조회합니다.
	 * @param address 도로명 주소
	 * @param radius 검색 반경(위/경도 단위, 기본값: 0.1)
	 * @param limit 조회할 음식점 개수(기본값: 20)
	 * @return 주변 음식점 리스트
	 */
	@GetMapping("/nearby")
	public ResponseEntity<List<RestaurantDetailResDto>> getNearbyRestaurants(
			@RequestParam String address,
			@RequestParam(defaultValue = "0.1") Double radius,
			@RequestParam(defaultValue = "20") Integer limit) {
		List<Restaurant> restaurantList = serviceV3.getnearRestaurants(address, radius, limit);
		return ResponseEntity.ok(mapper.toDto2(restaurantList));
	}

	/**
	 * 행정동 기반으로 주변 음식점 목록을 조회합니다.
	 * @param emd 행정동
	 * @return 주변 음식점 리스트
	 */
	@GetMapping("/emd")
	public ResponseEntity<List<RestaurantDetailResDto>> findRestaurantsByEmd(
			@RequestParam String emd,
			@RequestParam(defaultValue = "20") Integer limit) {
		List<Restaurant> restaurantList = serviceV3.getEmdRestaurants(emd);
		List<Restaurant> filteredRestaurantByLimit = serviceV3.limitFilter(limit, restaurantList);
		
		return ResponseEntity.ok(mapper.toDto2(filteredRestaurantByLimit));
	}

	/**
	 * 음식점 데이터를 생성합니다.
	 * @param dto 음식점 생성 요청 DTO
	 */
	@PostMapping("/create")
	public ResponseEntity<RestaurantDto> createRestaurantData(@RequestBody RestaurantDto dto) {
		RestaurantDto createdRestaurant = mapper.toDto(serviceV3.save(mapper.toEntity(dto)));
		URI location = URI.create("/view/" + createdRestaurant.id());
		
		return ResponseEntity.created(location).body(createdRestaurant);
	}

	/**
	 * 음식점 데이터를 수정합니다.
	 * @param dto 음식점 수정 요청 DTO
	 */
	@PatchMapping("/update")
	public ResponseEntity<RestaurantDto> updateRestaurantData(@RequestBody RestaurantDto dto) throws MergePropertyException {
		Restaurant entity = mapper.toEntity(dto);
		RestaurantDto updateRestaurant = mapper.toDto(serviceV3.update(entity, dto.id()));
		return ResponseEntity.accepted().body(updateRestaurant);
	}

	/**
	 * 음식점 데이터를 삭제합니다.
	 * @param restaurantId 삭제할 음식점 ID
	 */
	@DeleteMapping("/delete")
	public ResponseEntity<RestaurantDto> deleteRetaurantData(@RequestParam Long restaurantId) {
		RestaurantDto deletedRestaurant = mapper.toDto(serviceV3.delete(restaurantId));
		return ResponseEntity.ok(deletedRestaurant);
	}
	
	/**
	 * ID 기반으로 대상 음식점의 상세 정보를 조회합니다.
	 *
	 * @param id 대상 가게 ID
	 * @return 주변 음식점 리스트
	 */
	@GetMapping("/view/{id}")
	public ResponseEntity<RestaurantFullDto> findRestaurantsById(
			@PathVariable("id") Long id) {
		Restaurant restaurant = serviceV3.getById(id);
		return ResponseEntity.ok(mapper.toDto4(restaurant));
	}
	
	/**
	 * 전체 음식점 좌표를 랜덤으로 조회합니다.
	 *
	 * @param limit 조회할 음식점 좌표 개수(기본값: 100)
	 * @return 음식점 좌표 리스트
	 */
	@GetMapping("/random")
	public ResponseEntity<List<RestaurantFullDto>> getRandomFullRestaurantList(
			@RequestParam(defaultValue = "100") Integer limit) {
		// 일단 전부 가져오기
		List<Restaurant> restaurantList = serviceV3.getAll();
		List<Restaurant> filteredRestaurantByLimit = serviceV3.limitFilter(limit, restaurantList);
		
		return ResponseEntity.ok(mapper.toDto4(filteredRestaurantByLimit));
	}
}
