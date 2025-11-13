package com.good_restaurant.restaurant.repository;

import com.good_restaurant.restaurant.domain.Restaurant;
import com.good_restaurant.restaurant.domain.RestaurantComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantCommentRepository extends JpaRepository<RestaurantComment, Long> {
	List<RestaurantComment> findByRestaurant(Restaurant restaurant);
	
	Page<RestaurantComment> findByRestaurant_Id(Long id, Pageable pageable);
}