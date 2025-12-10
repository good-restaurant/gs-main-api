package com.good_restaurant.restaurant.service;

import com.good_restaurant.restaurant.domain.Road도로명주소한글;
import com.good_restaurant.restaurant.service.A_base.BaseCRUD;

import java.util.List;

public interface RoadNameKoreanService extends BaseCRUD<Road도로명주소한글, String> {
	
	Road도로명주소한글 searchCandidates(String query);
	
	String buildFullAddress(Road도로명주소한글 d);
	
	List<String> getProvinceList();
	
	List<String> getCityList();
	
	List<String> getTownList();
	
	List<String> searchProvinces(String query, int limit);
	
	List<String> searchCities(String query, int limit);
	
	List<String> searchTowns(String query, int limit);
	
	List<String> getTownListByCity(String cityQuery);
}
