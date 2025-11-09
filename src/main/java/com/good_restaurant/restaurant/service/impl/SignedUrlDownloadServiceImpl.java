package com.good_restaurant.restaurant.service.impl;

import com.good_restaurant.restaurant.service.SignedUrlDownloadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignedUrlDownloadServiceImpl implements SignedUrlDownloadService {
	
	private final RestTemplate restTemplate;
	
	@Value("${s3object.object-server-url}")
	private String storageServerUrl;
	
	@Override
	public String generateDownloadUrl(String objectUrl) {
		try {
			// presigned URL 요청
			URI presignUri = URI.create(storageServerUrl + "/api/files/presign-download?key=" + objectUrl);
			
			ResponseEntity<String> response = restTemplate.getForEntity(presignUri, String.class);
			
			if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
				throw new IllegalStateException("Failed to get presigned download URL");
			}
			
			log.info("[Download Presign] key={}, URL={}", objectUrl, response.getBody());
			return response.getBody().trim();
			
		} catch (Exception e) {
			log.error("[Download Error] {}", e.getMessage(), e);
			throw new RuntimeException("Failed to generate download URL", e);
		}
	}
}
