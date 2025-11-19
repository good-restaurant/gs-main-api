package com.good_restaurant.restaurant.service;

import com.good_restaurant.restaurant.domain.Restaurant;
import com.good_restaurant.restaurant.service.A_Exception.MergePropertyException;
import com.good_restaurant.restaurant.service.A_base.BaseCRUD;

import java.util.List;


/**
 * 새 DTO와 스키마를 위해 재작성한 RestaurantService
 */
public interface RestaurantServiceV3 extends BaseCRUD<Restaurant, Long> {
	
	List<Restaurant> randomLimit(int limit);
	
	List<Restaurant> getnearRestaurants(String address, double radius, int limit);
	
	List<Restaurant> getEmdRestaurants(String emd);
	
	List<Restaurant> limitFilter(Integer limit, List<Restaurant> restaurants);
	
	
	List<Restaurant> getLocatedRestaurants(Double lat, Double lon, Double radius, Integer limit);
	
	/**
	 * 행정동(EMD) 이름 기반 부분일치 검색
	 *
	 * @param emd 검색어(행정동명)
	 * @return 부분일치로 조회된 식당 목록
	 */
	List<Restaurant> getEmdLikeRestaurants(String emd);
	
	/**
	 * 식당명 기반 부분일치 검색
	 *
	 * @param searchQuery 검색어(식당명)
	 * @return 부분일치로 조회된 식당 목록
	 */
	List<Restaurant> getRestaurantsName(String searchQuery);
	
	/**
	 * 도로명 주소 기반 부분일치 검색
	 * 주소 스타일과 숫자/공백 차이를 고려하여 완화 검색 수행
	 *
	 * @param searchQuery 검색어(도로명 주소)
	 * @return 부분일치로 조회된 식당 목록
	 */
	List<Restaurant> getRoadNameAddressRestaurants(String searchQuery);
	
	/**
	 * 여러 검색 결과 리스트를 합쳐서 중복 제거(ID 기준)
	 *
	 * @param lists N개의 Restaurant 리스트
	 * @return 중복 제거된 Restaurant 리스트
	 */
	List<Restaurant> removeDistinct(List<Restaurant>... lists);
}
