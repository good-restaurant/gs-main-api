package com.good_restaurant.restaurant.controller;

import com.good_restaurant.restaurant.domain.Restaurant;
import com.good_restaurant.restaurant.domain.RestaurantPicture;
import com.good_restaurant.restaurant.dto.RestaurantCoordinateResDto;
import com.good_restaurant.restaurant.service.SignedUrlUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/signed-upload")
public class SignedUrlFileUpload {
	
	
	private final SignedUrlUploadService signedUrlUploadService;
	
	@PostMapping(
			value = "/restaurant/{restaurantId}",
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE
	)
	public ResponseEntity<RestaurantPicture> uploadPicture(
			@PathVariable Long restaurantId,
			@RequestParam("file") MultipartFile file) {
		
		// 레스토랑 정보를 가져다 쓸 수 있도록 entity 형태로 입력
		Restaurant restaurant = Restaurant.builder().id(restaurantId).build();
		
		// 인코딩된 파일명으로 업로드 처리
		RestaurantPicture savedPicture = signedUrlUploadService.uploadPicture(restaurant, file);
		
		
		return ResponseEntity.ok(savedPicture);
	}
	
}
