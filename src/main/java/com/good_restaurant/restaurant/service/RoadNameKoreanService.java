package com.good_restaurant.restaurant.service;

import com.good_restaurant.restaurant.domain.Road도로명주소한글;
import com.good_restaurant.restaurant.service.A_base.BaseCRUD;

public interface RoadNameKoreanService extends BaseCRUD<Road도로명주소한글, String> {
	
	Road도로명주소한글 searchCandidates(String query);
	
	String buildFullAddress(Road도로명주소한글 d);
}
