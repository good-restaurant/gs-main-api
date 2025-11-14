package com.good_restaurant.restaurant.controller;


import com.good_restaurant.restaurant.domain.Restaurant;
import com.good_restaurant.restaurant.dto.RestaurantFullDto;
import com.good_restaurant.restaurant.mapper.RestaurantMapper;
import com.good_restaurant.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/restaurant-admin")
public class RestaurantAdminController {
	
	private final RestaurantService service;
	private final RestaurantMapper mapper;
	
	/**
	 * 페이지 조건(page, size, sort)으로 식당 목록 조회
	 */
	@GetMapping("/view")
	public ResponseEntity<Page<RestaurantFullDto>> getPageableRestaurants(
			Pageable pageable // 페이지 번호, 크기, 정렬 조건
	) {
		Page<Restaurant> page = service.getRestaurantsAsPage(pageable);
		Page<RestaurantFullDto> result = page.map(mapper::toFullDto);
		return ResponseEntity.ok(result);
	}
	
	/**
	 * 식당 ID 로 단일 조회
	 */
	@GetMapping("/view/{id}")
	public ResponseEntity<RestaurantFullDto> getRestaurantsById(
			@PathVariable Long id // 식당 ID
	) {
		Restaurant restaurant = service.findRestaurantsById(id);
		RestaurantFullDto result = mapper.toFullDto(restaurant);
		return ResponseEntity.ok(result);
	}
	
	/**
	 * 식당 신규 생성
	 */
	@PostMapping
	public ResponseEntity<RestaurantFullDto> createRestaurant(
			@RequestBody RestaurantFullDto dto // 생성할 식당 정보
	) {
		Restaurant entity = mapper.toEntity(dto);
		Restaurant saved = service.save(entity);
		return ResponseEntity.ok(mapper.toFullDto(saved));
	}
	
	/**
	 * 식당 정보 수정
	 */
	@PutMapping("/{id}")
	public ResponseEntity<RestaurantFullDto> updateRestaurant(
			@PathVariable Long id, // 식당 ID
			@RequestBody RestaurantFullDto dto // 수정할 식당 정보
	) {
		Restaurant entity = mapper.toEntity(dto);
		Restaurant updated = service.updateRestaurant(id, entity);
		return ResponseEntity.ok(mapper.toFullDto(updated));
	}
	
	/**
	 * 식당 삭제
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<RestaurantFullDto> deleteRestaurant(
			@PathVariable Long id // 식당 ID
	) {
		Restaurant deleted = service.delete(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT)
				       .body(mapper.toFullDto(deleted));
	}
}

