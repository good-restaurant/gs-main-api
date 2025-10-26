package com.good_restaurant.restaurant.repository;

import com.good_restaurant.restaurant.domain.Restaurant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

	List<Restaurant> findTop100ByOrderByIdAsc();

	@Query(value = """
        SELECT *
        FROM good_restaurant.restaurants r
        WHERE r.lat IS NOT NULL AND r.lon IS NOT NULL
        ORDER BY random()
        LIMIT :limit
        """, nativeQuery = true)
	List<Restaurant> pickRandom(@Param("limit") int limit);
}