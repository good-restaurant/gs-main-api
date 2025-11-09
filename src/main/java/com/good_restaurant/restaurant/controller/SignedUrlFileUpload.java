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
		
		// 여기서는 예시용 Mock 객체
		Restaurant restaurant = Restaurant.builder().id(restaurantId).build();
		
		RestaurantPicture savedPicture = signedUrlUploadService.uploadPicture(restaurant, file);
		
		return ResponseEntity.ok(savedPicture);
	}
	
}
