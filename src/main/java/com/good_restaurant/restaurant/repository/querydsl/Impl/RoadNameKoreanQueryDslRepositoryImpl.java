package com.good_restaurant.restaurant.repository.querydsl.Impl;

import com.good_restaurant.restaurant.domain.QRoad도로명주소한글;
import com.good_restaurant.restaurant.domain.Road도로명주소한글;
import com.good_restaurant.restaurant.repository.querydsl.RoadNameKoreanQueryDslRepository;
import com.querydsl.core.Tuple;
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
	
	@Override
	public List<String> findAllDistinctBy시도명() {
		QRoad도로명주소한글 road = QRoad도로명주소한글.road도로명주소한글;
		
		return queryFactory
				       .select(road.시도명)
				       .distinct()
				       .from(road)
				       .orderBy(road.시도명.asc())
				       .fetch();
	}
	
	@Override
	public List<String> findAllDistinctBy시군구명() {
		QRoad도로명주소한글 road = QRoad도로명주소한글.road도로명주소한글;
		
		return queryFactory
				       .select(road.시군구명)
				       .distinct()
				       .from(road)
				       .orderBy(road.시군구명.asc())
				       .fetch();
	}
	
	@Override
	public List<Tuple> findAllDistinctBy법정읍면동리명() {
		
		QRoad도로명주소한글 road = QRoad도로명주소한글.road도로명주소한글;
		
		return queryFactory
				       .select(road.법정읍면동명, road.법정리명)
				       .distinct()
				       .from(road)
				       .fetch();
	}
	
	@Override
	public List<Tuple> findAllDistinctBy법정리명() {
		QRoad도로명주소한글 road = QRoad도로명주소한글.road도로명주소한글;
		
		return queryFactory
				       .select(road.법정읍면동명, road.법정리명)
				       .distinct()
				       .from(road)
				       .where(road.법정리명.isNotNull())
				       .orderBy(road.법정리명.asc())
				       .fetch();
	}
}
