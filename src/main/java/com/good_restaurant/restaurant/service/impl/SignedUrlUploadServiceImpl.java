package com.good_restaurant.restaurant.service.impl;

import com.good_restaurant.restaurant.domain.Restaurant;
import com.good_restaurant.restaurant.domain.RestaurantPicture;
import com.good_restaurant.restaurant.repository.RestaurantPictureRepository;
import com.good_restaurant.restaurant.service.SignedUrlUploadService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignedUrlUploadServiceImpl implements SignedUrlUploadService {
	
	private final RestTemplate restTemplate;
	private final RestaurantPictureRepository pictureRepository;
	
	@Value("${s3object.object-server-url}")
	private String storageServerUrl;
	
	@Transactional
	public RestaurantPicture uploadPicture(Restaurant restaurant, MultipartFile file) {
		try {
			URI presignUri = URI.create(storageServerUrl
					                            + "/api/files/presign-upload-json?filename=" + file.getOriginalFilename()
					                            + "&contentType=" + file.getContentType());
			
			ResponseEntity<Map> response = restTemplate.getForEntity(presignUri, Map.class);
			if (!response.getStatusCode().is2xxSuccessful()) {
				throw new RuntimeException("Failed to get presigned upload URL");
			}
			
			String objectKey = (String) response.getBody().get("objectKey");
			String uploadUrl = (String) response.getBody().get("url");
			log.info("[Presign] Received key: {}", objectKey);
			
			// PUT 업로드
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType(file.getContentType()));
			HttpEntity<byte[]> entity = new HttpEntity<>(file.getBytes(), headers);
			ResponseEntity<Void> uploadResponse =
					restTemplate.exchange(URI.create(uploadUrl), HttpMethod.PUT, entity, Void.class);
			
			if (!uploadResponse.getStatusCode().is2xxSuccessful()) {
				throw new IllegalStateException("Ceph upload failed: " + uploadResponse.getStatusCode());
			}
			
			// DB 저장
			RestaurantPicture picture = RestaurantPicture.builder()
					                            .pictureUuid(UUID.fromString(objectKey.split("_")[0]))
					                            .originalFilename(file.getOriginalFilename())
					                            .s3ObjectKey(objectKey)
					                            .restaurant(restaurant)
					                            .build();
			
			return pictureRepository.save(picture);
			
		} catch (Exception e) {
			log.error("[Upload Error] {}", e.getMessage(), e);
			throw new RuntimeException("Upload failed", e);
		}
	}
	
}

