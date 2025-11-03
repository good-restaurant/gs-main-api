package com.good_restaurant.restaurant.service;

import com.good_restaurant.restaurant.dto.RestaurantCoordinateResDto;
import com.good_restaurant.restaurant.dto.RestaurantCreateReqDto;
import com.good_restaurant.restaurant.dto.RestaurantDetailResDto;
import java.util.List;

/**
 * 음식점 도메인의 핵심 비즈니스 규약을 정의하는 서비스 인터페이스입니다.
 * <p>
 * DI 가이드:
 * <ul>
 *   <li>상위 계층(컨트롤러 등)은 이 인터페이스에만 의존합니다.</li>
 *   <li>실제 구현은 {@code service.impl} 패키지의
 *       {@link com.good_restaurant.restaurant.service.impl.RestaurantServiceImpl} 이 담당합니다.</li>
 *   <li>스프링은 구현 클래스를 빈으로 등록하고, 생성자 주입을 통해 이 인터페이스 타입으로 주입합니다.</li>
 *   <li>이 구조는 구현 교체(캐싱/정책 변경 등)와 단위 테스트(모킹) 시 유연성을 제공합니다.</li>
 * </ul>
 */
public interface RestaurantService {

    /**
     * 전체 음식점 좌표를 랜덤으로 조회합니다.
     *
     * @param limit 조회할 음식점 좌표 개수(최대 개수 제한)
     * @return 음식점 좌표 리스트
     */
    List<RestaurantCoordinateResDto> getEntireRestaurantCoordinates(int limit);

    /**
     * 도로명 주소를 기준으로 지정한 반경 내의 주변 음식점 상세 정보를 조회합니다.
     *
     * @param address 도로명 주소(지오코딩 대상)
     * @param radius  검색 반경(위/경도 단위)
     * @param limit   조회할 최대 개수
     * @return 주변 음식점 상세 리스트. 주소 지오코딩 실패 시 빈 리스트를 반환할 수 있습니다.
     */
    List<RestaurantDetailResDto> getNearbyRestaurants(String address, double radius, int limit);

    /**
     * 행정동(EMD) 기준으로 음식점 상세 목록을 조회합니다.
     *
     * @param emd   행정동
     * @param limit 조회할 최대 개수
     * @return 해당 행정동의 음식점 상세 리스트
     */
    List<RestaurantDetailResDto> findRestaurantsByEmd(String emd, int limit);

    /**
     * 음식점 기본 정보 및 상세 정보를 생성합니다.
     *
     * @param dto 음식점 생성 요청 DTO
     */
    void createRestaurantData(RestaurantCreateReqDto dto);
	
	/**
	 * QueryDSL 기반으로 도로명 주소 중심 주변 음식점 상세 조회
	 * (기존 findNearbyWithDetail 대체)
	 */
	List<RestaurantDetailResDto> getNearbyRestaurantsQueryDsl(String address, double radius, int limit);
	
	/**
	 * QueryDSL 기반으로 행정동(EMD) 기준 음식점 상세 목록을 랜덤 조회합니다.
	 * (기존 findRestaurantsByEmd 대체)
	 */
	List<RestaurantDetailResDto> findRestaurantsByEmdQueryDsl(String emd, int limit);
	
	/**
	 * QueryDSL 기반으로 전체 음식점 좌표를 랜덤 조회합니다.
	 * (기존 JPQL pickRandom 대체)
	 */
	List<RestaurantCoordinateResDto> getEntireRestaurantCoordinatesApplication(int limit);
}
