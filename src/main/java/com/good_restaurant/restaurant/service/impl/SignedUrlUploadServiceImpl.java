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

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
			// 1 파일명 및 Content-Type 인코딩
			String encodedFilename = URLEncoder.encode(file.getOriginalFilename(), StandardCharsets.UTF_8)
					                         .replace("+", "%20");
			String encodedContentType = URLEncoder.encode(file.getContentType(), StandardCharsets.UTF_8)
					                            .replace("+", "%20");
			
			// 2️ Presigned URL 요청용 URI 생성
			String presignUrl = storageServerUrl
					                    + "/api/files/presign-upload-json"
					                    + "?filename=" + encodedFilename
					                    + "&contentType=" + encodedContentType;
			
			URI presignUri = URI.create(presignUrl);
			
			// 3 Presigned URL 요청
			ResponseEntity<Map> response = restTemplate.getForEntity(presignUri, Map.class);
			if (!response.getStatusCode().is2xxSuccessful()) {
				throw new RuntimeException("Failed to get presigned upload URL");
			}
			
			// 4️ 업로드 URL 및 Key 획득
			String objectKey = (String) response.getBody().get("objectKey");
			String uploadUrl = (String) response.getBody().get("url");
			log.info("[Presign] Received key: {}", objectKey);
			
			// 5️ 실제 PUT 업로드 수행
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType(file.getContentType()));
			HttpEntity<byte[]> entity = new HttpEntity<>(file.getBytes(), headers);
			
			ResponseEntity<Void> uploadResponse =
					restTemplate.exchange(URI.create(uploadUrl), HttpMethod.PUT, entity, Void.class);
			
			if (!uploadResponse.getStatusCode().is2xxSuccessful()) {
				throw new IllegalStateException("Ceph upload failed: " + uploadResponse.getStatusCode());
			}
			
			// 6️ DB 저장
			RestaurantPicture picture = RestaurantPicture.builder()
					                            .pictureUuid(UUID.fromString(objectKey.split("_")[0]))
					                            .originalFilename(file.getOriginalFilename())
					                            .s3ObjectKey(URLEncoder.encode(objectKey))
					                            .restaurant(restaurant)
					                            .build();
			
			return pictureRepository.save(picture);
			
		} catch (Exception e) {
			log.error("[Upload Error] {}", e.getMessage(), e);
			throw new RuntimeException("Upload failed", e);
		}
	}
	
}

