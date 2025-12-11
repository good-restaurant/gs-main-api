package com.good_restaurant.restaurant.repository.querydsl;

import com.good_restaurant.restaurant.domain.Road도로명주소한글;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface RoadNameKoreanQueryDslRepository {
	
	List<Road도로명주소한글> searchWide(String query, PageRequest of);
	
	List<String> findAllDistinctBy시도명();
	
	List<String> findAllDistinctBy시군구명();
	
	List<Tuple> findAllDistinctBy법정읍면동리명();
	
	List<Tuple> findAllDistinctBy법정리명();
	
	List<Road도로명주소한글> findRoadNameBy도로명(String query);
}
