package com.good_restaurant.repository;

import com.good_restaurant.entity.GoodRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodRestaurantRepository extends JpaRepository<GoodRestaurant, Long> {
}
