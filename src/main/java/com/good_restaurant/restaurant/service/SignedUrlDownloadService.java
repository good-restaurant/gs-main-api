package com.good_restaurant.restaurant.service;

import com.good_restaurant.restaurant.domain.RestaurantPicture;

public interface SignedUrlDownloadService {
	
	String generateDownloadUrl(String objectUrl);
}
