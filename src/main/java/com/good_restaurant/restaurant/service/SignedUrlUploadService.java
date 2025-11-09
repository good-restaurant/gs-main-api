package com.good_restaurant.restaurant.service;

import com.good_restaurant.restaurant.domain.Restaurant;
import com.good_restaurant.restaurant.domain.RestaurantPicture;
import org.springframework.web.multipart.MultipartFile;

public interface SignedUrlUploadService {
	
	RestaurantPicture uploadPicture(Restaurant restaurant, MultipartFile file);
}
