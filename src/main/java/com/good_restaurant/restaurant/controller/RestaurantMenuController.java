package com.good_restaurant.restaurant.controller;


import com.good_restaurant.restaurant.domain.Restaurant;
import com.good_restaurant.restaurant.domain.RestaurantMenu;
import com.good_restaurant.restaurant.dto.RestaurantFullDto;
import com.good_restaurant.restaurant.dto.RestaurantMenuDto;
import com.good_restaurant.restaurant.mapper.RestaurantMenuMapper;
import com.good_restaurant.restaurant.service.RestaurantMenuService;
import com.good_restaurant.restaurant.service.RestaurantService;
import com.good_restaurant.restaurant.service.SignedUrlDownloadService;
import com.good_restaurant.restaurant.service.SignedUrlUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/restaurant-menu")
@RequiredArgsConstructor
public class RestaurantMenuController {
	
	private final RestaurantMenuService service;
	private final RestaurantMenuMapper mapper;
	
	/**
	 * 페이지 조건(page, size, sort)으로 메뉴 목록 조회
	 */
	@GetMapping("/view")
	public ResponseEntity<Page<RestaurantMenuDto>> getPageableMenus(
			Pageable pageable  // 페이지 번호, 크기, 정렬 조건
	) {
		Page<RestaurantMenu> page = service.getMenusAsPage(pageable);
		Page<RestaurantMenuDto> result = page.map(mapper::toDto);
		return ResponseEntity.ok(result);
	}
	
	/**
	 * 메뉴 ID 로 단일 조회
	 */
	@GetMapping("/view/{id}")
	public ResponseEntity<RestaurantMenuDto> getMenuById(
			@PathVariable Long id // 메뉴 ID
	) {
		RestaurantMenu menu = service.findMenuById(id);
		RestaurantMenuDto result = mapper.toDto(menu);
		return ResponseEntity.ok(result);
	}
	
	/**
	 * 메뉴 신규 생성
	 */
	@PostMapping
	public ResponseEntity<RestaurantMenuDto> createMenu(
			@RequestBody RestaurantMenuDto dto // 생성할 메뉴 정보
	) {
		RestaurantMenu entity = mapper.toEntity(dto);
		RestaurantMenu saved = service.save(entity);
		return ResponseEntity.ok(mapper.toDto(saved));
	}
	
	/**
	 * 메뉴 정보 수정
	 */
	@PutMapping("/{id}")
	public ResponseEntity<RestaurantMenuDto> updateMenu(
			@PathVariable Long id, // 메뉴 ID
			@RequestBody RestaurantMenuDto dto // 수정할 메뉴 정보
	) {
		RestaurantMenu entity = mapper.toEntity(dto);
		RestaurantMenu updated = service.updateMenu(id, entity);
		return ResponseEntity.ok(mapper.toDto(updated));
	}
	
	/**
	 * 메뉴 삭제
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<RestaurantMenuDto> deleteMenu(
			@PathVariable Long id // 메뉴 ID
	) {
		RestaurantMenu deleted = service.delete(id);
		return ResponseEntity
				       .status(HttpStatus.NO_CONTENT)
				       .body(mapper.toDto(deleted));
	}
}
