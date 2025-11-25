package com.good_restaurant.restaurant.service;

import com.good_restaurant.restaurant.domain.RestaurantPicture;
import com.good_restaurant.restaurant.service.A_base.BaseCRUD;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface RestaurantPictureService  extends BaseCRUD<RestaurantPicture, Long> {
	
	RestaurantPicture getPicture(Long pictureId);
	
	RestaurantPicture uploadRestaurantPicture(Long restaurantId, MultipartFile file);
	
	Page<RestaurantPicture> getRecentPicture(Pageable pageable);
	
	RestaurantPicture delete(Long pictureId);
	
}
