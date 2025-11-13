package com.good_restaurant.restaurant.controller;

import com.good_restaurant.restaurant.domain.RestaurantComment;
import com.good_restaurant.restaurant.domain.RestaurantCommentFullDto;
import com.good_restaurant.restaurant.dto.RestaurantCommentDto;
import com.good_restaurant.restaurant.mapper.RestaurantCommentMapper;
import com.good_restaurant.restaurant.service.RestaurantCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
	
	@PostMapping
	public ResponseEntity<RestaurantCommentFullDto> createComment(
			@RequestBody RestaurantCommentFullDto dto
	) {
		// dto → entity
		RestaurantComment entity = mapper.toEntity(dto);
		
		RestaurantComment saved = service.createComment(entity);
		
		return ResponseEntity.ok(mapper.toFullDto(saved));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<RestaurantCommentFullDto> updateComment(
			@PathVariable Long id,
			@RequestBody RestaurantCommentFullDto dto
	) {
		RestaurantComment entity = mapper.toEntity(dto);
		RestaurantComment updated = service.updateComment(id, entity);
		
		return ResponseEntity.ok(mapper.toFullDto(updated));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<RestaurantCommentFullDto> deleteComment(@PathVariable Long id) {
		RestaurantComment comment = service.delete(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(mapper.toFullDto(comment));
	}
}
