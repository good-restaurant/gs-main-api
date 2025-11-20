package com.good_restaurant.restaurant.repository.querydsl;

import com.good_restaurant.restaurant.domain.Road도로명주소한글;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface RoadNameKoreanQueryDslRepository {
	
	List<Road도로명주소한글> searchWide(String query, PageRequest of);
}
