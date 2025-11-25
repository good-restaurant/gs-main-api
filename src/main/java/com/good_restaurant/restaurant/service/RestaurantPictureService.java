package com.good_restaurant.restaurant.service;

import com.good_restaurant.restaurant.domain.RestaurantPicture;
import com.good_restaurant.restaurant.dto.RestaurantPictureDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RestaurantPictureService {
	
	RestaurantPicture getPicture(Long pictureId);
	
	RestaurantPicture uploadRestaurantPicture(Long restaurantId, MultipartFile file);
	
	Page<RestaurantPicture> getRecentPicture(Pageable pageable);
}
