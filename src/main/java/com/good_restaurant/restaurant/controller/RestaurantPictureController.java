package com.good_restaurant.restaurant.controller;

import com.good_restaurant.restaurant.domain.RestaurantComment;
import com.good_restaurant.restaurant.domain.RestaurantCommentFullDto;
import com.good_restaurant.restaurant.domain.RestaurantPicture;
import com.good_restaurant.restaurant.dto.RestaurantPictureDto;
import com.good_restaurant.restaurant.mapper.RestaurantPictureMapper;
import com.good_restaurant.restaurant.service.RestaurantPictureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/restaurant-picture")
public class RestaurantPictureController {

	private final RestaurantPictureService service;
	private final RestaurantPictureMapper mapper;
	
	/**
	 * 최근 사진 목록을 페이지 형태로 조회합니다.
	 *
	 * @param pageable 페이지 정보 (size: 20 / 50 / 100) 권장
	 * @return 코멘트 목록 페이지
	 */
	@GetMapping("/recent")
	public ResponseEntity<Page<RestaurantPictureDto>> getRecentComments(
//			기본값 최근 20개씩
			@PageableDefault(size = 20) Pageable pageable
	) {
		
		// 최근 코멘트 페이지 조회
		Page<RestaurantPicture> page = service.getRecentPicture(pageable);
		Page<RestaurantPictureDto> dtoPage = page.map(mapper::toDto);
		return ResponseEntity.ok(dtoPage);
	}
	
}
