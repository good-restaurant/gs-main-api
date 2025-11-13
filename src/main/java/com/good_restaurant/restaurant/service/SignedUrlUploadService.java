package com.good_restaurant.restaurant.service;

import com.good_restaurant.restaurant.domain.Restaurant;
import com.good_restaurant.restaurant.dto.UploadResult;
import org.springframework.web.multipart.MultipartFile;

public interface SignedUrlUploadService {
	
//	UploadResult uploadResult(Restaurant restaurant, MultipartFile file);
	
	UploadResult uploadResult(String originalFilename, String contentType, byte[] fileBytes);
}
