package com.good_restaurant.restaurant.service.impl;

import com.good_restaurant.restaurant.dto.UploadResult;
import com.good_restaurant.restaurant.service.SignedUrlUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
	
	@Value("${s3object.object-server-url}")
	private String storageServerUrl;
	
	// DB는 알지 못하고 presigned URL 발급 + PUT 업로드만 수행
	@Override
	public UploadResult uploadResult(String originalFilename, String contentType, byte[] fileBytes) {
		
		try {
			String encodedFilename = URLEncoder.encode(originalFilename, StandardCharsets.UTF_8).replace("+", "%20");
			String encodedContentType = URLEncoder.encode(contentType, StandardCharsets.UTF_8).replace("+", "%20");
			
			String presignUrl = storageServerUrl
					                    + "/api/files/presign-upload-json"
					                    + "?filename=" + encodedFilename
					                    + "&contentType=" + encodedContentType;
			
			URI presignUri = URI.create(presignUrl);
			ResponseEntity<Map> response = restTemplate.getForEntity(presignUri, Map.class);
			
			if (!response.getStatusCode().is2xxSuccessful()) {
				throw new RuntimeException("Failed to get presigned upload URL");
			}
			
			String objectKey = (String) response.getBody().get("objectKey");
			String uploadUrl = (String) response.getBody().get("url");
			UUID uuid = UUID.fromString(objectKey.split("_")[0]);
			
			// 실제 PUT 업로드
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType(contentType));
			HttpEntity<byte[]> entity = new HttpEntity<>(fileBytes, headers);
			
			ResponseEntity<Void> uploadResponse =
					restTemplate.exchange(URI.create(uploadUrl), HttpMethod.PUT, entity, Void.class);
			
			if (!uploadResponse.getStatusCode().is2xxSuccessful()) {
				throw new IllegalStateException("Ceph upload failed");
			}
			
			return new UploadResult(uuid, objectKey, uploadUrl);
			
		} catch (Exception e) {
			throw new RuntimeException("Upload failed: " + e.getMessage(), e);
		}
	}
}

