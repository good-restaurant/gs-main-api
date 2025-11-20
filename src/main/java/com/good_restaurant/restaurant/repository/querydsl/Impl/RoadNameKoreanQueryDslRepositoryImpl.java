package com.good_restaurant.restaurant.repository.querydsl.Impl;

import com.good_restaurant.restaurant.domain.QRoad도로명주소한글;
import com.good_restaurant.restaurant.domain.Road도로명주소한글;
import com.good_restaurant.restaurant.repository.querydsl.RoadNameKoreanQueryDslRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@RequiredArgsConstructor
public class RoadNameKoreanQueryDslRepositoryImpl implements RoadNameKoreanQueryDslRepository {
	
	private final JPAQueryFactory queryFactory;
	
	@Override
	public List<Road도로명주소한글> searchWide(String q, PageRequest of) {
		QRoad도로명주소한글 road = QRoad도로명주소한글.road도로명주소한글;
		
		return queryFactory
				       .selectFrom(road)
				       .where(
						       road.시도명.contains(q)
								       .or(road.시군구명.contains(q))
								       .or(road.도로명.contains(q))
				       )
				       .orderBy(road.도로명.asc())
				       .offset(of.getOffset())
				       .limit(20)
				       .fetch();
	}
	
}
