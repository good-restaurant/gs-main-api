package com.good_restaurant.restaurant.service.impl;

import com.good_restaurant.restaurant.domain.Restaurant;
import com.good_restaurant.restaurant.dto.GeocodeResultDto;
import com.good_restaurant.restaurant.dto.RestaurantCoordinateResDto;
import com.good_restaurant.restaurant.dto.RestaurantDetailResDto;
import com.good_restaurant.restaurant.dto.RestaurantDto;
import com.good_restaurant.restaurant.mapper.RestaurantMapper;
import com.good_restaurant.restaurant.repository.RestaurantRepository;
import com.good_restaurant.restaurant.service.GeocodingService;
import com.good_restaurant.restaurant.service.RestaurantService;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
/**
 * RestaurantService 구현체.
 * <p>
 * DI 설명:
 * <ul>
 *   <li>생성자 주입(@RequiredArgsConstructor)으로 Repository 들과 {@link com.good_restaurant.restaurant.service.GeocodingService} 인터페이스를 의존성으로 받습니다.</li>
 *   <li>상위 계층(컨트롤러 등)은 {@link com.good_restaurant.restaurant.service.RestaurantService} 인터페이스에만 의존합니다.</li>
 *   <li>구현체 교체(예: 캐싱 추가/외부 API 전략 변경)나 테스트 시 모킹이 용이해집니다.</li>
 * </ul>
 * 스프링 빈 등록은 {@link org.springframework.stereotype.Service} 애너테이션으로 처리됩니다.
 */
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
//    private final RestaurantDetailRepository restaurantDetailRepository;
    private final GeocodingService geocodingService;
    private final RestaurantMapper restaurantMapper;
	
	/**
     * 전체 음식점 좌표를 랜덤으로 조회합니다.
     * @param limit 조회할 음식점 좌표 제한 개수
     * @return 음식점 좌표 리스트
     */
    @Override
    @Transactional(readOnly = true)
    public List<RestaurantCoordinateResDto> getEntireRestaurantCoordinates(int limit) {
        Pageable limitPage = PageRequest.of(0, limit);

        return restaurantMapper.toDto3(restaurantRepository.pickRandom(limitPage));
    }

    /**
     * 도로명 주소를 기반으로 주변 음식점 목록을 조회합니다.
     * @param address 도로명 주소
     * @param radius 검색 반경(위/경도 단위)
     * @param limit 조회할 음식점 개수
     * @return 주변 음식점 리스트
     */
    @Override
    @Transactional(readOnly = true)
    public List<RestaurantDetailResDto> getNearbyRestaurants(String address, double radius, int limit) {
        // 주소로부터 위도/경도 얻기
        GeocodeResultDto geoResult = geocodingService.geocode(address);

        // 위도/경도 정보를 얻지 못한 경우 빈 리스트 반환(예: 잘못된 주소)
        if (geoResult == null) {
            return Collections.emptyList();
        }

        BigDecimal minLat = geoResult.getLat().subtract(BigDecimal.valueOf(radius));
        BigDecimal maxLat = geoResult.getLat().add(BigDecimal.valueOf(radius));
        BigDecimal minLon = geoResult.getLon().subtract(BigDecimal.valueOf(radius));
        BigDecimal maxLon = geoResult.getLon().add(BigDecimal.valueOf(radius));

        Pageable limitPage = PageRequest.of(0, limit);

        return restaurantRepository.findNearbyWithDetail(
                minLat, maxLat, minLon, maxLon,
                geoResult.getLat(), geoResult.getLon(),
                limitPage
        );
    }

    /**
     * 행정동 기반으로 주변 음식점 목록을 조회합니다.
     * @param emd 행정동
     * @return 주변 음식점 리스트
     */
    @Override
    @Transactional(readOnly = true)
    public List<RestaurantDetailResDto> findRestaurantsByEmd(String emd, int limit) {
        Pageable limitPage = PageRequest.of(0, limit);
        return restaurantRepository.findRestaurantsByEmd(emd, limitPage);
    }
	
	/**
	 * 어플리케이션 레이어에서 랜덤 추출 및 DTO 변환 처리
	 * (기존 JPQL pickRandom 대체)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<RestaurantCoordinateResDto> getEntireRestaurantCoordinatesApplication(int limit) {
		
		// 1️ 모든 음식점 로드
		List<Restaurant> allRestaurants = restaurantRepository.findAll().stream()
				                                  .collect(Collectors.toList());
		
		// 2 섞기 (DB 대신 애플리케이션에서 랜덤 정렬)
		Collections.shuffle(allRestaurants);
		
		// 3 상위 limit개만 추출
		List<Restaurant> limitedRestaurants = allRestaurants.stream()
				                                      .limit(limit)
				                                      .toList();
		
		// 4 DTO 매핑
		return restaurantMapper.toDto(limitedRestaurants);
	}
	
	
	/**
  * QueryDSL 기반으로 도로명 주소 중심 주변 음식점 상세 조회
  * (기존 findNearbyWithDetail 대체)
  */
 public List<RestaurantDetailResDto> getNearbyRestaurantsQueryDsl(String address, double radius, int limit) {
     GeocodeResultDto geoResult = geocodingService.geocode(address);
     if (geoResult == null) return Collections.emptyList();
        
     BigDecimal minLat = geoResult.getLat().subtract(BigDecimal.valueOf(radius));
     BigDecimal maxLat = geoResult.getLat().add(BigDecimal.valueOf(radius));
     BigDecimal minLon = geoResult.getLon().subtract(BigDecimal.valueOf(radius));
     BigDecimal maxLon = geoResult.getLon().add(BigDecimal.valueOf(radius));
     Pageable limitPage = PageRequest.of(0, limit);
        
     return restaurantMapper.toDto2(
             restaurantRepository.findNearbyRestaurantsWithDetail(
                 minLat, maxLat, minLon, maxLon,
                 geoResult.getLat(), geoResult.getLon(),
                 limitPage
             )
     );
 }
    
 /**
  * QueryDSL 기반으로 행정동(EMD) 기준 음식점 상세 목록을 랜덤 조회합니다.
  * (기존 findRestaurantsByEmd 대체)
  */
 public List<RestaurantDetailResDto> findRestaurantsByEmdQueryDsl(String emd, int limit) {
     Pageable limitPage = PageRequest.of(0, limit);
     return restaurantMapper.toDto2(restaurantRepository.findRestaurantsByEmdRandom(emd, limitPage));
 }
	
	/**
	 * 음식점 데이터를 생성합니다.
	 *
	 * @param dto 음식점 생성 요청 DTO
	 */
	@Transactional
	@Override
	public void createRestaurantData(RestaurantDto dto) {
		Restaurant newRestaurant = restaurantRepository.save(restaurantMapper.toEntity(dto)
		);
	}
}
