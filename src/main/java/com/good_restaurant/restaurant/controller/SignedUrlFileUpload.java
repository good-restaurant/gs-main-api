package com.good_restaurant.restaurant.controller;

import com.good_restaurant.restaurant.domain.Restaurant;
import com.good_restaurant.restaurant.domain.RestaurantMenu;
import com.good_restaurant.restaurant.domain.RestaurantPicture;
import com.good_restaurant.restaurant.dto.MenuUploadRequest;
import com.good_restaurant.restaurant.dto.RestaurantMenuDto;
import com.good_restaurant.restaurant.mapper.RestaurantMenuMapper;
import com.good_restaurant.restaurant.service.RestaurantMenuService;
import com.good_restaurant.restaurant.service.RestaurantPictureService;
import com.good_restaurant.restaurant.service.SignedUrlUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/signed-upload")
public class SignedUrlFileUpload {
	
	//	entity와 관련없는 업로드 서비스가 필요할 때 사용
	private final SignedUrlUploadService signedUrlUploadService;
	
	private final RestaurantPictureService restaurantPictureService;
	private final RestaurantMenuService restaurantMenuService;
	private final RestaurantMenuMapper mapper;
	
	@PostMapping(
			value = "/restaurant/{restaurantId}",
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE
	)
	public ResponseEntity<RestaurantPicture> uploadRestaurantPicture(
			@PathVariable Long restaurantId,
			@RequestParam("file") MultipartFile file) {
		
		// 레스토랑 정보를 가져다 쓸 수 있도록 entity 형태로 입력
		Restaurant restaurant = Restaurant.builder().id(restaurantId).build();
		
		// 인코딩된 파일명으로 업로드 처리
		RestaurantPicture savedPicture = restaurantPictureService.uploadRestaurantPicture(restaurantId, file);
		
		return ResponseEntity.ok(savedPicture);
	}
	
	@PostMapping(
			value = "/restaurantMenu/{menuId}",
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE
	)
	public ResponseEntity<RestaurantMenuDto> uploadRestaurantMenuPicture(
			@PathVariable Long menuId,
			@RequestParam("file") MultipartFile file) {
		
		// 레스토랑 정보를 가져다 쓸 수 있도록 entity 형태로 입력
		RestaurantMenu restaurantMenu = RestaurantMenu.builder().id(menuId).build();
		
		// 인코딩된 파일명으로 업로드 처리
		RestaurantMenu savedPicture = restaurantMenuService.uploadRestaurantPicture(restaurantMenu, file);
		RestaurantMenuDto savedDto = mapper.toDto(savedPicture);
		
		return ResponseEntity.ok(savedDto);
	}
	
	@PostMapping(
			value = "/restaurantMenu/batch/upload",
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE
	)
	public ResponseEntity<List<RestaurantMenuDto>> uploadMenuPicturesBatch(
			@RequestPart("metadata") List<MenuUploadRequest> metadata,
			@RequestPart("files") List<MultipartFile> files
	) {
		
		if (metadata.size() != files.size()) {
			throw new IllegalArgumentException("Metadata count and file count must match");
		}
		
		List<RestaurantMenuDto> result = new ArrayList<>();
		
		for (int i = 0; i < metadata.size(); i++) {
			MenuUploadRequest meta = metadata.get(i);
			MultipartFile file = files.get(i);
			
			RestaurantMenu saved = restaurantMenuService.uploadRestaurantPicture(
					RestaurantMenu.builder().id(meta.menuId()).build(),
					file
			);
			
			result.add(mapper.toDto(saved));
		}
		
		return ResponseEntity.ok(result);
	}
	
}
