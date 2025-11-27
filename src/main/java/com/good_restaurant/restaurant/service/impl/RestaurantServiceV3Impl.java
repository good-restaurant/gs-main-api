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

import java.util.*;

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
		
		// 도로명 주소로 확인한 기준좌표점
		double lat = geocodeResultDto.getLat().doubleValue();
		double lon = geocodeResultDto.getLon().doubleValue();
		
		// 반경(m) -> 위경도 차이(degree)로 처리
		double deltaLat = meterToLatitudeDegree(1000 * radius);          // 거리(m) 입력
		double deltaLon = meterToLongitudeDegree(1000 * radius, lat);    // 거리(m) + 위도 기준 입력
		
		
		// 최종 검색 영역 (Bounding Box)
		double minLat = lat - deltaLat;
		double maxLat = lat + deltaLat;
		double minLon = lon - deltaLon;
		double maxLon = lon + deltaLon;
		
		
		return repository.findByLatBetweenAndLonBetween(
				minLat, maxLat, minLon, maxLon,
				PageRequest.of(0, limit)
		);
	}
	
	@Override
	public List<Restaurant> getEmdRestaurants(String emd) {
		return repository.findByEmdKorNmContaining(emd);
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
		
		// 반경(m) → 위경도 차이(degree)
		double deltaLat = meterToLatitudeDegree(radius);
		double deltaLon = meterToLongitudeDegree(radius, lat);
		
		// 검색 영역 (Bounding Box)
		double minLat = lat - deltaLat;
		double maxLat = lat + deltaLat;
		double minLon = lon - deltaLon;
		double maxLon = lon + deltaLon;
		
		// findByJPARepository
		return repository.findByLatBetweenAndLonBetween(
				minLat, maxLat, minLon, maxLon,
				PageRequest.of(0, limit)
		);
		
	}
	
	@Override
	public List<Restaurant> getEmdLikeRestaurants(String emd) {
		return repository.findByEmdKorNmContainsOrEmdKorNmLike(emd, emd);
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
	
	
	@Override
	public List<Restaurant> getRestaurantsName(String searchQuery) {
		// ## 방어 코드
		if (searchQuery == null || searchQuery.isBlank()) {
			return randomLimit(100);
		}
		
		return repository.findByRestaurantNameContainingIgnoreCase(searchQuery.trim());
	}
	
	@Override
	public List<Restaurant> getRoadNameAddressRestaurants(String searchQuery) {
		
		// ## 방어 코드
		if (searchQuery == null || searchQuery.isBlank()) {
			return List.of();
		}
		
		String q = normalizeRoadName(searchQuery);
		
		// ## 1차: 기본 검색
		List<Restaurant> result = repository.findByAddressContainingIgnoreCase(q);
		if (!result.isEmpty()) return result;
		
		// ## 2차: 숫자 공백 제거 후 재검색 (ex: "저동길 165" → "저동길165")
		String noSpaceNum = q.replaceAll("([가-힣A-Za-z])\\s+(\\d)", "$1$2");
		if (!noSpaceNum.equals(q)) {
			result = repository.findByAddressContainingIgnoreCase(noSpaceNum);
			if (!result.isEmpty()) return result;
		}
		
		// ## 3차: 숫자만 남기기 (건물번호 앞자리로만 찾을 때)
		String onlyNum = q.replaceAll("[^0-9]", "");
		if (!onlyNum.isEmpty()) {
			result = repository.findByAddressContainingIgnoreCase(onlyNum);
			if (!result.isEmpty()) return result;
		}
		
		// ## 4차: 도로명만 추출 후 검색 (저동길 / 저동로 / 저동대로 등)
		String roadNameOnly = q.replaceAll("[0-9].*", ""); // 숫자 이후 삭제
		roadNameOnly = roadNameOnly.trim();
		if (!roadNameOnly.isEmpty() && !roadNameOnly.equals(q)) {
			result = repository.findByAddressContainingIgnoreCase(roadNameOnly);
			if (!result.isEmpty()) return result;
		}
		
		return List.of();
	}
	
	
	// ## 검색어 전처리
	private String normalizeRoadName(String input) {
		String q = input.trim();
		
		// ## 한글 자모 분리된 경우 대비 (Normalization Form C)
		q = java.text.Normalizer.normalize(q, java.text.Normalizer.Form.NFC);
		
		// ## 주소에서 흔히 등장하는 괄호 제거
		q = q.replaceAll("\\([^)]*\\)", "");  // (저동)
		
		// ## 불필요한 중복 공백 제거
		q = q.replaceAll("\\s+", " ");
		
		return q;
	}
	
	@Override
	public List<Restaurant> removeDistinct(List<Restaurant>... lists) {
		// ## 결과 저장용 Set (ID로 중복 제거)
		Map<Long, Restaurant> map = new LinkedHashMap<>();
		
		// ## 인자로 들어온 모든 리스트 순회
		for (List<Restaurant> list : lists) {
			if (list == null) continue;
			for (Restaurant r : list) {
				if (r == null) continue;
				map.put(r.getId(), r); // ID 기준 중복 제거
			}
		}
		
		// ## 순서 보존 + 중복 제거된 결과
		return new ArrayList<>(map.values());
	}
	
	@Override
	public List<Restaurant> getAddressRestaurants(String province, String city, String town) {
		
		// 문자열 공백 방지
		String normalizedProvince = normalize(province);
		String normalizedCity = normalize(city);
		String normalizedTown = normalize(town);
		
		// 읍 면 동 전용
		if (normalizedTown != null && (normalizedProvince == null && normalizedCity == null)) {
			List<Restaurant> restaurantList = repository.findByEmdKorNm(normalizedTown);
			if (!restaurantList.isEmpty()) return restaurantList;
		}
		
		// 읍 면 동 + 시
		if (normalizedTown != null && normalizedCity != null && normalizedProvince == null) {
			List<Restaurant> restaurantList = repository.findByEmdKorNmAndSigKorNm(normalizedTown, normalizedCity);
			if (!restaurantList.isEmpty()) return restaurantList;
		}
		
		// 시 전용
		if (normalizedCity != null && (normalizedProvince == null && normalizedTown == null)) {
			List<Restaurant> restaurantList = repository.findBySigKorNm(normalizedCity);
			if (!restaurantList.isEmpty()) return restaurantList;
		}
		
		// 완전 일치
		if (normalizedProvince != null && normalizedCity != null && normalizedTown != null) {
			List<Restaurant> restaurantList = repository.findByCtpKorNmAndSigKorNmAndEmdKorNm(normalizedProvince, normalizedCity, normalizedTown);
			if (!restaurantList.isEmpty()) return restaurantList;
		}
		
		// 시·군·구 단위
		if (normalizedProvince != null && normalizedCity != null) {
			List<Restaurant> restaurantList = repository.findByCtpKorNmAndSigKorNm(normalizedProvince, normalizedCity);
			if (!restaurantList.isEmpty()) return restaurantList;
		}
		
		// 도 단위
		if (normalizedProvince != null) {
			List<Restaurant> restaurantList = repository.findByCtpKorNm(normalizedProvince);
			if (!restaurantList.isEmpty()) return restaurantList;
		}
		
		// 아무것도 없으면 랜덤 1000개
		return randomLimit(1000);
	}
	
	private String normalize(String s) {
		if (s == null) return null;
		s = s.trim();
		return s.isBlank() ? null : s;
	}
	
}
