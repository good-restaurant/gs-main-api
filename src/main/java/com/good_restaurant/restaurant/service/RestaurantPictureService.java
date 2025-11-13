package com.good_restaurant.restaurant.service;

import com.good_restaurant.restaurant.domain.RestaurantPicture;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RestaurantPictureService {
	
	RestaurantPicture getPicture(Long pictureId);
	
	RestaurantPicture uploadRestaurantPicture(Long restaurantId, MultipartFile file);
	
}
