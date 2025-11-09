package com.good_restaurant.restaurant.controller;

import com.good_restaurant.restaurant.domain.RestaurantPicture;
import com.good_restaurant.restaurant.repository.RestaurantPictureRepository;
import com.good_restaurant.restaurant.service.RestaurantPictureService;
import com.good_restaurant.restaurant.service.SignedUrlDownloadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/signed-download")
@RequiredArgsConstructor
public class SignedUrlFileDownload {
	
	private final SignedUrlDownloadService signedUrlDownloadService;
	private final RestaurantPictureService service;
	
	/**
	 * 특정 이미지 ID로 signed URL을 요청합니다.
	 */
	@GetMapping("/{pictureId}")
	public ResponseEntity<String> getSignedDownloadUrl(@PathVariable Long pictureId) {
		RestaurantPicture picture = service.getPicture(pictureId);
		String signedUrl = signedUrlDownloadService.generateDownloadUrl(picture.getS3ObjectKey());
		return ResponseEntity.ok(signedUrl);
	}
}
