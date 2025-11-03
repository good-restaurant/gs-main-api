package com.good_restaurant.restaurant.repository;

import com.good_restaurant.restaurant.domain.QRestaurant;
import com.good_restaurant.restaurant.domain.QRestaurantDetail;
import com.good_restaurant.restaurant.dto.RestaurantDetailResDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RestaurantQueryRepository {
	
	private final JPAQueryFactory queryFactory;
	
	public List<RestaurantDetailResDto> findNearbyWithDetail(
			BigDecimal minLat, BigDecimal maxLat,
			BigDecimal minLon, BigDecimal maxLon,
			BigDecimal centerLat, BigDecimal centerLon,
			Pageable pageable
	) {
		QRestaurant r = QRestaurant.restaurant;
		QRestaurantDetail d = QRestaurantDetail.restaurantDetail;
		
		return queryFactory
				       .select(Projections.constructor(RestaurantDetailResDto.class,
						       r.id,
						       r.restaurantName,
						       r.address,
						       r.category,
						       r.lon,
						       r.lat,
						       d.menu,
						       d.phoneNumber
				       ))
				       .from(r)
				       .leftJoin(r.detail, d)
				       .where(r.lat.between(minLat, maxLat)
						              .and(r.lon.between(minLon, maxLon)))
				       .orderBy(Expressions.numberTemplate(Double.class,
						       "POWER({0} - {1}, 2) + POWER({2} - {3}, 2)",
						       r.lat, centerLat, r.lon, centerLon).asc())
				       .offset(pageable.getOffset())
				       .limit(pageable.getPageSize())
				       .fetch();
	}
	
	public List<RestaurantDetailResDto> findByEmd(String emd, Pageable pageable) {
		QRestaurant r = QRestaurant.restaurant;
		QRestaurantDetail d = QRestaurantDetail.restaurantDetail;
		
		return queryFactory
				       .select(Projections.constructor(RestaurantDetailResDto.class,
						       r.id,
						       r.restaurantName,
						       r.address,
						       r.category,
						       r.lon,
						       r.lat,
						       d.menu,
						       d.phoneNumber
				       ))
				       .from(r)
				       .leftJoin(r.detail,  d)
				       .where(r.emdKorNm.eq(emd))
				       .orderBy(r.id.asc())
				       .offset(pageable.getOffset())
				       .limit(pageable.getPageSize())
				       .fetch();
	}
}