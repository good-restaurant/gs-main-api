package com.good_restaurant.restaurant.repository;

import com.good_restaurant.restaurant.domain.Road도로명주소한글;
import com.good_restaurant.restaurant.repository.querydsl.RoadNameKoreanQueryDslRepository;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoadNameKoreanRepository extends JpaRepository<Road도로명주소한글, String>, RoadNameKoreanQueryDslRepository {
	
	List<Road도로명주소한글> findTop10By도로명ContainingOrderBy도로명Asc(String q);
	
	List<Road도로명주소한글> findBy도로명And건물본번And건물부번(String road, Integer mainNo, Integer subNo);
	
	List<Road도로명주소한글> searchWide(String query, PageRequest of);
	
	List<Road도로명주소한글> findDistinct시도명By시도명Containing(String keyword, Pageable pageable);
	
	List<Road도로명주소한글> findDistinct시군구명By시군구명Containing(String keyword, Pageable pageable);
	
	List<Road도로명주소한글> findDistinct법정읍면동명By법정읍면동명Containing(String keyword, Pageable pageable);
	
	List<Road도로명주소한글> findDistinct법정리명By법정리명Containing(String keyword, Pageable pageable);
	
	
	List<String> findAllDistinctBy시도명();
	
	List<String> findAllDistinctBy시군구명();
	
	List<Tuple> findAllDistinctBy법정읍면동리명();
	
	List<Tuple> findAllDistinctBy법정리명();
}