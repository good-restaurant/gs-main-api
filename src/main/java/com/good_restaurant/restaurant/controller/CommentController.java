package com.good_restaurant.restaurant.controller;

import com.good_restaurant.restaurant.domain.RestaurantComment;
import com.good_restaurant.restaurant.domain.RestaurantCommentFullDto;
import com.good_restaurant.restaurant.dto.RestaurantCommentDto;
import com.good_restaurant.restaurant.mapper.RestaurantCommentMapper;
import com.good_restaurant.restaurant.service.RestaurantCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurant-comment")
public class CommentController {
	
	private final RestaurantCommentService service;
	private final RestaurantCommentMapper mapper;
	
	/**
	 * 해당 식당의 코멘트 목록을 Pageable하게 조회합니다.
	 *
	 * @return 코멘트 목록
	 */
	@GetMapping("/view/{id}")
	public ResponseEntity<List<RestaurantCommentDto>> getRestaurantComments(
			@PathVariable Long id,
			Pageable pageable
	) {
		// 서비스 → 엔티티 목록
		List<RestaurantComment> comments = service.getCommentsByRestaurantId(id, pageable);
		
		// 엔티티 → DTO
		List<RestaurantCommentDto> result = mapper.toDto(comments);
		
		return ResponseEntity.ok(result);
	}
	
	/**
	 * 대상 코멘트만 단독으로 조회합니다.
	 *
	 * @return 대상 코멘트
	 */
	@GetMapping("/view-comment/{id}")
	public ResponseEntity<RestaurantCommentFullDto> getCommentById(@PathVariable Long id) {
		
		RestaurantComment entity = service.getCommentById(id);
		
		RestaurantCommentFullDto dto = mapper.toFullDto(entity);
		
		return ResponseEntity.ok(dto);
	}
	
	/**
	 * 최근 코멘트를 페이지 형태로 조회합니다.
	 *
	 * @param pageable 페이지 정보 (size: 20 / 50 / 100) 권장
	 * @return 코멘트 목록 페이지
	 */
	@GetMapping("/recent")
	public ResponseEntity<Page<RestaurantCommentFullDto>> getRecentComments(
//			기본값 최근 20개씩
			@PageableDefault(size = 20)  Pageable pageable
	) {
		
		// 최근 코멘트 페이지 조회
		Page<RestaurantComment> page = service.getRecentComments(pageable);
		
		// 엔티티 -> DTO 변환
		Page<RestaurantCommentFullDto> dtoPage = page.map(mapper::toFullDto);
		
		return ResponseEntity.ok(dtoPage);
	}
	
	@PostMapping
	public ResponseEntity<RestaurantCommentFullDto> createComment(
			@RequestBody RestaurantCommentFullDto dto
	) {
		// dto → entity
		RestaurantComment entity = mapper.toEntity(dto);
		
		RestaurantComment saved = service.createComment(entity);
		
		return ResponseEntity.ok(mapper.toFullDto(saved));
	}
	
	// AdminController에서만 수정 및 삭제가능
}
