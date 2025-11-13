package com.good_restaurant.restaurant.service;

import com.good_restaurant.restaurant.domain.RestaurantComment;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RestaurantCommentService {
	
	List<RestaurantComment> getCommentsByRestaurantId(Long restaurantId, Pageable pageable);
	
	RestaurantComment getCommentById(Long commentId);
	
	RestaurantComment createComment(RestaurantComment entity);
	
	RestaurantComment updateComment(Long id, RestaurantComment entity);
	
	RestaurantComment delete(Long id);
}
