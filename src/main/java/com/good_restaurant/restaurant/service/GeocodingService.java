package com.good_restaurant.restaurant.service;

import com.good_restaurant.restaurant.dto.GeocodeResultDto;

/**
 * 주소 문자열을 위도/경도 좌표로 변환(지오코딩)하는 기능의 계약을 정의하는 서비스 인터페이스입니다.
 * <p>
 * DI 가이드:
 * <ul>
 *   <li>상위 계층(서비스/컨트롤러 등)은 이 인터페이스에만 의존합니다.</li>
 *   <li>실제 구현은 {@code service.impl} 패키지의
 *       {@link com.good_restaurant.restaurant.service.impl.GeocodingServiceImpl} 이 담당합니다.</li>
 *   <li>스프링은 구현 클래스를 빈으로 등록하고, 생성자 주입을 통해 이 인터페이스 타입으로 주입합니다.</li>
 *   <li>이 구조는 외부 지오코딩 공급자 교체나 테스트(모킹/스텁) 시 유연성을 제공합니다.</li>
 * </ul>
 */
public interface GeocodingService {

    /**
     * 주어진 도로명 주소를 지오코딩하여 좌표를 반환합니다.
     *
     * <p>구현에 따라 외부 API 오류, 잘못된 주소 등으로 좌표를 얻지 못하면
     * {@code null}을 반환할 수 있습니다. 호출 측에서는 null 처리(대체 로직/빈 결과 반환 등)를 고려하세요.</p>
     *
     * @param address 도로명 주소(지오코딩 대상)
     * @return 지오코딩 결과 DTO. 실패 시 {@code null} 일 수 있습니다.
     */
    GeocodeResultDto geocode(String address);
}
