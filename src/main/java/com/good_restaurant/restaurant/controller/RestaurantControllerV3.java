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
		
		return ResponseEntity.ok(mapper.toCoordinateDto(restaurantList));
	}
	
	/**
	 * 전체 음식점 좌표를 랜덤으로 조회합니다.
	 *
	 * @param limit 조회할 음식점 좌표 개수(기본값: 100)
	 * @return 음식점 좌표 리스트
	 */
	@GetMapping("/list-search")
	public ResponseEntity<List<RestaurantCoordinateResDto>> getEntireRestaurantCoordinates(
			@RequestParam String emd,
			@RequestParam(defaultValue = "100") Integer limit) {
		List<Restaurant> restaurantList = serviceV3.getEmdLikeRestaurants(emd);
		List<Restaurant> filteredRestaurantByLimit = serviceV3.limitFilter(limit, restaurantList);
		
		return ResponseEntity.ok(mapper.toCoordinateDto(filteredRestaurantByLimit));
	}
	
	/**
	 * 도로명 주소를 기반으로 주변 음식점 목록을 조회합니다.
	 * @param address 도로명 주소
	 * @param radius 검색 반경(위/경도 단위, 기본값: 0.1) -> 0.1 = 100M 로 설정하여 처리
	 * @param limit 조회할 음식점 개수(기본값: 20)
	 * @return 주변 음식점 리스트
	 */
	@GetMapping("/nearby")
	public ResponseEntity<List<RestaurantDetailResDto>> getNearbyRestaurants(
			@RequestParam String address,
			@RequestParam(defaultValue = "0.1") Double radius,
			@RequestParam(defaultValue = "20") Integer limit) {
		List<Restaurant> restaurantList = serviceV3.getnearRestaurants(address, radius, limit);
		return ResponseEntity.ok(mapper.toDetailResDto(restaurantList));
	}
	
	/**
	 * 위치좌표를 기반으로 주변 음식점 목록을 조회합니다.
	 *
	 * @param lat 좌표정보 위도 (37.566535)
	 * @param lon 좌표정보 경도 (126.9779692) - 서울시청 좌표임
	 * @param radius  검색 반경(미터 단위로 처리)
	 * @param limit   조회할 음식점 개수(기본값: 20)
	 * @return 주변 음식점 리스트
	 */
	@GetMapping("/location")
	public ResponseEntity<List<RestaurantDetailResDto>> getNearbyRestaurantsByLocation(
			@RequestParam Double lat,    // 위도
			@RequestParam Double lon,    // 경도
			@RequestParam(defaultValue = "250") Double radius,
			@RequestParam(defaultValue = "20") Integer limit) {
		List<Restaurant> restaurantList = serviceV3.getLocatedRestaurants(lat, lon, radius, limit);
		return ResponseEntity.ok(mapper.toDetailResDto(restaurantList));
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
		
		return ResponseEntity.ok(mapper.toDetailResDto(filteredRestaurantByLimit));
	}
	
	/**
	 * searchQuery 는 일종의 통합검색처럼 작동하는 영역입니다.
	 * 몇 가지 조회 로직에 의해 나온 조회결과를 or 조건으로 다 가져옵니다.
	 * 그래도 중복은 뺍니다.
	 *
	 * @param searchQuery 행정동
	 * @return 주변 음식점 리스트
	 */
	@GetMapping("/search")
	public ResponseEntity<List<RestaurantDetailResDto>> findRestaurantsByTotalQuery(
			@RequestParam String searchQuery,
			@RequestParam(defaultValue = "20") Integer limit) {
		List<Restaurant> restaurantList = serviceV3.getRestaurantsName(searchQuery);
		List<Restaurant> restaurantList2 = serviceV3.getEmdRestaurants(searchQuery);
		List<Restaurant> restaurantList3 = serviceV3.getRoadNameAddressRestaurants(searchQuery);
		List<Restaurant> restaurantDistinctRemoved = serviceV3.removeDistinct(
				restaurantList,
				restaurantList2,
				restaurantList3
		);
		
		List<Restaurant> filteredRestaurantByLimit = serviceV3.limitFilter(limit, restaurantDistinctRemoved);
		
		return ResponseEntity.ok(mapper.toDetailResDto(filteredRestaurantByLimit));
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
		return ResponseEntity.ok(mapper.toFullDto(restaurant));
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
		
		return ResponseEntity.ok(mapper.toFullDto(filteredRestaurantByLimit));
	}
}
