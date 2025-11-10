package com.good_restaurant.restaurant.service.impl;

import com.good_restaurant.restaurant.domain.Restaurant;
import com.good_restaurant.restaurant.dto.GeocodeResultDto;
import com.good_restaurant.restaurant.repository.RestaurantRepository;
import com.good_restaurant.restaurant.service.A_Exception.MergePropertyException;
import com.good_restaurant.restaurant.service.A_base.BaseCRUD;
import com.good_restaurant.restaurant.service.A_base.ServiceHelper;
import com.good_restaurant.restaurant.service.GeocodingService;
import com.good_restaurant.restaurant.service.RestaurantServiceV3;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RestaurantServiceV3Impl implements RestaurantServiceV3, BaseCRUD<Restaurant, Long> {
	
	private final RestaurantRepository repository;
	private final ServiceHelper<Restaurant, Long> serviceHelper;
	private final GeocodingService geocodingService;
	
	@Override
	public JpaRepository<Restaurant, Long> getRepository() {
		return this.repository;
	}
	
	@Override
	public Restaurant delete(Long aLong) {
		Optional<Restaurant> optional = repository.findById(aLong);
		repository.deleteById(aLong);
		return optional.orElse(null);
	}
	
	@Override
	public Long getEntityId(Restaurant entity) {
		return entity.getId();
	}
	
//	@Override
//	public List<Restaurant> getAll() {
//		List<Restaurant> restaurants = repository.findAll();
//
//		// Batch size initialize
//		restaurants.forEach(r -> {
//			r.getRestaurantMenus().size();
//			r.getRestaurantPictures().size();
//			r.getRestaurantComments().size();
//		});
//		return restaurants;
//	}
	
	@Override
	public Restaurant updateRule(Restaurant source, Restaurant target) throws MergePropertyException {
		return serviceHelper.updateRule(source, target);
	}
	
	@Override
	public List<Restaurant> randomLimit(int limit) {
		return repository.getRandomLimit(limit);
	}
	
	@Override
	public List<Restaurant> getnearRestaurants(String address, double radius, int limit) {
		
		GeocodeResultDto geocodeResultDto = geocodingService.geocode(address);
		
		BigDecimal minLat = geocodeResultDto.getLat().subtract(BigDecimal.valueOf(radius));
		BigDecimal maxLat = geocodeResultDto.getLat().add(BigDecimal.valueOf(radius));
		BigDecimal minLon = geocodeResultDto.getLon().subtract(BigDecimal.valueOf(radius));
		BigDecimal maxLon = geocodeResultDto.getLon().add(BigDecimal.valueOf(radius));
		
		return repository.findNearbyRestaurantsWithDetail(
				minLat, maxLat, minLon, maxLon,
				BigDecimal.valueOf(geocodeResultDto.getLat().doubleValue()),
				BigDecimal.valueOf(geocodeResultDto.getLon().doubleValue()),
				PageRequest.of(0, limit)
		);
	}
	
	@Override
	public List<Restaurant> getEmdRestaurants(String emd) {
		return repository.findByEmdKorNm(emd);
	}
	
	@Override
	public List<Restaurant> limitFilter(Integer limit, List<Restaurant> restaurants) {
		// 방어 코드
		if (restaurants == null || restaurants.isEmpty() || limit == null || limit <= 0) {
			return List.of(); // 혹은 Collections.emptyList()
		}
		
		// 개수가 limit 이하라면 그냥 그대로
		if (restaurants.size() <= limit) {
			return List.copyOf(restaurants); // 또는 new ArrayList<>(restaurants)
		}
		
		// 원본 리스트 건드리지 않기 위해 복사본 생성
		List<Restaurant> shuffled = new java.util.ArrayList<>(restaurants);
		
		// 랜덤 섞기
		java.util.Collections.shuffle(shuffled);
		
		// 앞에서부터 limit 개만 반환
		return shuffled.subList(0, limit);
	}
	
	@Override
	public List<Restaurant> getLocatedRestaurants(Double lat, Double lon, Double radius, Integer limit) {
		
		// ## 반경(m) → 위경도 차이(degree)
		double deltaLat = meterToLatitudeDegree(radius);
		double deltaLon = meterToLongitudeDegree(radius, lat);
		
		// ## 검색 영역 (Bounding Box)
		double minLat = lat - deltaLat;
		double maxLat = lat + deltaLat;
		double minLon = lon - deltaLon;
		double maxLon = lon + deltaLon;
		
		// ## 쿼리 실행 (QueryDSL or JPQL 가능)
		return repository.findByLatBetweenAndLonBetween(
				minLat, maxLat, minLon, maxLon,
				PageRequest.of(0, limit)
		);
		
	}
	
	
	// ## 위도 변환 (1도 ≈ 111.32km)
	private double meterToLatitudeDegree(double meters) {
		return meters / 111_320.0;
	}
	
	// ## 경도 변환 (위도 따라 다름)
	private double meterToLongitudeDegree(double meters, double latitude) {
		double latRad = Math.toRadians(latitude);
		return meters / (111_320.0 * Math.cos(latRad));
	}
}
