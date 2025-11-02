package com.good_restaurant.restaurant.repository;

import com.good_restaurant.restaurant.domain.Restaurant;
import com.good_restaurant.restaurant.dto.RestaurantDetailResDto;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

	@Query("""
	  select r
	  from Restaurant r
	  where r.lat is not null and r.lon is not null
	  order by function('random')
	""")
	List<Restaurant> pickRandom(Pageable pageable);

	@Query("""
	  select new com.good_restaurant.restaurant.dto.RestaurantDetailResDto(
	    r.id, r.restaurantName, r.address, r.category, r.lon, r.lat,
	    d.menu, d.phoneNumber
	  )
	  from Restaurant r
	  left join r.detail d
	  where r.lat between :minLat and :maxLat
	    and r.lon between :minLon and :maxLon
	  order by (power(r.lat - :centerLat, 2) + power(r.lon - :centerLon, 2)) asc
	""")
	List<RestaurantDetailResDto> findNearbyWithDetail(
			BigDecimal minLat, BigDecimal maxLat,
			BigDecimal minLon, BigDecimal maxLon,
			BigDecimal centerLat, BigDecimal centerLon,
			Pageable pageable
	);

	@Query("""
	  select new com.good_restaurant.restaurant.dto.RestaurantDetailResDto(
	    r.id, r.restaurantName, r.address, r.category, r.lon, r.lat,
	    d.menu, d.phoneNumber
	  )
	  from Restaurant r
	  left join r.detail d
	  where r.emdKorNm = :emd
	  order by function('random')
	""")
	List<RestaurantDetailResDto> findRestaurantsByEmd(String emd, Pageable pageable);
}
