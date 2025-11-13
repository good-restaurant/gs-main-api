package com.good_restaurant.restaurant.repository;

import com.good_restaurant.restaurant.domain.RestaurantMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantMenuRepository extends JpaRepository<RestaurantMenu, Long> {
}