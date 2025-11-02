package com.good_restaurant.restaurant.repository;

import com.good_restaurant.restaurant.domain.RestaurantDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantDetailRepository extends JpaRepository<RestaurantDetail, Long> {

}