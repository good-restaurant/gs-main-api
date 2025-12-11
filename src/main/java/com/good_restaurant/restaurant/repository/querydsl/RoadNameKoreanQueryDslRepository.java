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

	/**
	 * 주어진 시군구명(세종특별자치시 포함)에 속한 법정 읍면동/리 명을 distinct 로 반환합니다.
	 * 반환 Tuple 스키마: (법정읍면동명, 법정리명)
	 */
	List<Tuple> findDistinctTownByCity(String cityName);
}
