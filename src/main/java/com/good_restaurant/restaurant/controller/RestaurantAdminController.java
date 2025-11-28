package com.good_restaurant.restaurant.controller;


import com.good_restaurant.restaurant.domain.Restaurant;
import com.good_restaurant.restaurant.domain.RestaurantComment;
import com.good_restaurant.restaurant.domain.RestaurantCommentFullDto;
import com.good_restaurant.restaurant.domain.RestaurantPicture;
import com.good_restaurant.restaurant.dto.RestaurantFullDto;
import com.good_restaurant.restaurant.dto.RestaurantPictureDto;
import com.good_restaurant.restaurant.mapper.RestaurantCommentMapper;
import com.good_restaurant.restaurant.mapper.RestaurantMapper;
import com.good_restaurant.restaurant.mapper.RestaurantPictureMapper;
import com.good_restaurant.restaurant.service.A_Exception.MergePropertyException;
import com.good_restaurant.restaurant.service.RestaurantCommentService;
import com.good_restaurant.restaurant.service.RestaurantPictureService;
import com.good_restaurant.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("/v1/restaurant-admin")
public class RestaurantAdminController {
	
	private final RestaurantService service;
	private final RestaurantMapper mapper;
	
	private final RestaurantPictureService pictureService;
	private final RestaurantPictureMapper pictureMapper;
	
	private final RestaurantCommentService commentService;
	private final RestaurantCommentMapper commentMapper;
	
	
	/**
	 * 페이지 조건(page, size, sort)으로 식당 목록 조회
	 */
	@GetMapping("/view")
	public ResponseEntity<Page<RestaurantFullDto>> getPageableRestaurants(Pageable pageable) {
		Page<Restaurant> page = service.getRestaurantsAsPage(pageable);
		Page<RestaurantFullDto> result = page.map(mapper::toFullDto);
		return ResponseEntity.ok(result);
	}
	
	/**
	 * 식당 ID 로 단일 조회
	 */
	@GetMapping("/view/{id}")
	public ResponseEntity<RestaurantFullDto> getRestaurantsById(@PathVariable Long id) {
		Restaurant restaurant = service.findRestaurantsById(id);
		RestaurantFullDto result = mapper.toFullDto(restaurant);
		return ResponseEntity.ok(result);
	}
	
	/**
	 * 식당 신규 생성
	 */
	@PostMapping
	public ResponseEntity<RestaurantFullDto> createRestaurant(@RequestBody RestaurantFullDto dto) {
		Restaurant entity = mapper.toEntity(dto);
		Restaurant saved = service.save(entity);
		return ResponseEntity.ok(mapper.toFullDto(saved));
	}
	
	/**
	 * 식당 정보 수정
	 */
	@PutMapping("/{id}")
	public ResponseEntity<RestaurantFullDto> updateRestaurant(
			@PathVariable Long id,
			@RequestBody RestaurantFullDto dto
	) {
		Restaurant entity = mapper.toEntity(dto);
		Restaurant updated = service.updateRestaurant(id, entity);
		return ResponseEntity.ok(mapper.toFullDto(updated));
	}
	
	/**
	 * 식당 삭제
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<RestaurantFullDto> deleteRestaurant(@PathVariable Long id) {
		Restaurant deleted = service.delete(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT)
				       .body(mapper.toFullDto(deleted));
	}
	
	//<editor-fold desc="사진 수정 삭제">
	/**
	 * 특정 사진 정보 수정
	 *
	 * @param pictureId 수정할 사진 ID
	 * @param dto       수정할 사진 정보
	 * @return 수정된 사진 정보
	 */
	@PutMapping("/picture/{pictureId}")
	public ResponseEntity<RestaurantPictureDto> updatePicture(
			@PathVariable Long pictureId,
			@RequestBody RestaurantPictureDto dto
	) throws MergePropertyException {
		RestaurantPicture entity = pictureMapper.toEntity(dto);
		RestaurantPicture updated = pictureService.update(entity,pictureId);
		return ResponseEntity.ok(pictureMapper.toDto(updated));
	}
	
	/**
	 * 특정 사진 삭제
	 *
	 * @param pictureId 삭제할 사진 ID
	 * @return 삭제된 사진 정보
	 */
	@DeleteMapping("/picture/{pictureId}")
	public ResponseEntity<RestaurantPictureDto> deletePicture(@PathVariable Long pictureId) {
		RestaurantPicture deleted = pictureService.delete(pictureId);
		return ResponseEntity.ok(pictureMapper.toDto(deleted));
	}
	//</editor-fold>
	
	
	//<editor-fold desc="코멘트 영역 수정 삭제">
	/**
	 * 특정 코멘트 정보 수정
	 *
	 * @param commentId 수정할 코멘트 ID
	 * @param dto       수정할 코멘트 정보
	 * @return 수정된 코멘트 정보
	 */
	@PutMapping("/comment/{commentId}")
	public ResponseEntity<RestaurantCommentFullDto> updateComment(
			@PathVariable Long commentId,
			@RequestBody RestaurantCommentFullDto dto
	) {
		RestaurantComment entity = commentMapper.toEntity(dto);
		RestaurantComment updated = commentService.updateComment(commentId, entity);
		return ResponseEntity.ok(commentMapper.toFullDto(updated));
	}
	
	/**
	 * 특정 코멘트 삭제
	 *
	 * @param commentId 삭제할 코멘트 ID
	 * @return 삭제된 코멘트 정보
	 */
	@DeleteMapping("/comment/{commentId}")
	public ResponseEntity<RestaurantCommentFullDto> deleteComment(@PathVariable Long commentId) {
		RestaurantComment deleted = commentService.delete(commentId);
		return ResponseEntity.ok(commentMapper.toFullDto(deleted));
	}
	//</editor-fold>
}

