package com.good_restaurant.restaurant.repository.querydsl.Impl;

import com.good_restaurant.restaurant.domain.QRestaurant;
import com.good_restaurant.restaurant.domain.QRestaurantDetail;
import com.good_restaurant.restaurant.domain.Restaurant;
import com.good_restaurant.restaurant.repository.querydsl.RestaurantQueryDslRepository;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Repository
@RequiredArgsConstructor
public class RestaurantQueryDslRepositoryImpl implements RestaurantQueryDslRepository {
	
	private final JPAQueryFactory queryFactory;
	
	private final QRestaurant r = QRestaurant.restaurant;
	private final QRestaurantDetail d = QRestaurantDetail.restaurantDetail;
	
	/**
	 * ✅ 1. 랜덤 음식점 좌표 조회 (fetch join 없음, N+1 없음)
	 * - detail 미접근 시 추가 select 발생하지 않음
	 * 1000개 이상이면 안 좋기 때문에 Deprecated
	 */
	@Deprecated
	@Override
	public List<Restaurant> findRandomRestaurantsQueryDsl(Pageable pageable) {
		return queryFactory
				       .selectFrom(r)
				       .where(r.lat.isNotNull().and(r.lon.isNotNull()))
				       .orderBy(Expressions.numberTemplate(Double.class, "random()").asc()) // PostgreSQL 내장함수
				       .offset(pageable.getOffset())
				       .limit(pageable.getPageSize())
				       .fetch();
	}
	
	/**
	 * ✅ 2. 주변 음식점 + 상세 정보 (fetch join으로 detail 포함)
	 * - 거리 기반 정렬 (lat/lon 차이 제곱합)
	 * - 단일 SQL, N+1 완전 제거
	 */
	@Override
	public List<Restaurant> findNearbyRestaurantsWithDetail(
			BigDecimal minLat, BigDecimal maxLat,
			BigDecimal minLon, BigDecimal maxLon,
			BigDecimal centerLat, BigDecimal centerLon,
			Pageable pageable
	) {
		return queryFactory
				       .selectFrom(r)
				       .leftJoin(r.detail, d).fetchJoin()
				       .where(r.lat.between(minLat, maxLat)
						              .and(r.lon.between(minLon, maxLon)))
				       .orderBy(
						       r.id.asc(), // DISTINCT ON 에 포함될 키
						       Expressions.numberTemplate(Double.class,
								       "(power({0} - {1}, 2) + power({2} - {3}, 2))",
								       r.lat, centerLat, r.lon, centerLon).asc()
				       )
				       .fetch();
	}
	
	/**
	 * ✅ 3. 행정동(EMD) 기반 랜덤 음식점 조회 + 상세 정보 포함
	 * - fetch join으로 N+1 완전 제거
	 * - random() 대신 랜덤 offset 방식으로 성능 최적화 가능
	 */
	@Override
	public List<Restaurant> findRestaurantsByEmdRandom(String emd, Pageable pageable) {
		
		// (Optional) count 기반 랜덤 offset 최적화
		Long totalCount = queryFactory.select(r.count()).from(r)
				                  .where(r.emdKorNm.eq(emd)
						                         .and(r.lat.isNotNull())
						                         .and(r.lon.isNotNull()))
				                  .fetchOne();
		
		long offset = 0L;
		if (totalCount != null && totalCount > pageable.getPageSize()) {
			offset = ThreadLocalRandom.current().nextLong(totalCount - pageable.getPageSize());
		}
		
		return queryFactory
				       .selectFrom(r).distinct()
				       .leftJoin(r.detail, d).fetchJoin()
				       .where(r.emdKorNm.eq(emd)
						              .and(r.lat.isNotNull())
						              .and(r.lon.isNotNull()))
				       .offset(offset)
				       .limit(pageable.getPageSize())
				       .fetch();
	}
}
