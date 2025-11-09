package com.good_restaurant.restaurant.repository;

import com.good_restaurant.restaurant.domain.RestaurantPicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantPictureRepository extends JpaRepository<RestaurantPicture, Long> {
}