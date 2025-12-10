package com.good_restaurant.restaurant.controller;

import com.good_restaurant.restaurant.mapper.RoadNameKoreanMapper;
import com.good_restaurant.restaurant.service.RoadNameKoreanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/address-list")
public class AddressListController {
	
	private final RoadNameKoreanService service;
	
	/**
	 * 시도 목록을 반환합니다.
	 *
	 * @return 시도명 리스트
	 */
	@GetMapping("/provinces")
	public ResponseEntity<List<String>> getProvinces() {
		return ResponseEntity.ok(service.getProvinceList());
	}
	
	/**
	 * 전체 주소의 시군구 목록을 반환합니다.
	 *
	 * @return 시군구 리스트
	 */
	@GetMapping("/cities")
	public ResponseEntity<List<String>> getCities(
	) {
		return ResponseEntity.ok(service.getCityList());
	}
	
	/**
	 * 전체 주소의 읍면동/리 목록을 반환합니다.
	 *
	 * @return 동/리 리스트
	 */
	@GetMapping("/towns")
	public ResponseEntity<List<String>> getTowns(
	) {
		return ResponseEntity.ok(service.getTownList());
	}
	
	/**
	 * 전체 주소의 읍면동/리 목록을 반환합니다.
	 *
	 * @return 동/리 리스트
	 */
	@GetMapping("/searchlist/town/")
	public ResponseEntity<List<String>> getTowns(
			@RequestParam("city") String cityQuery
	) {
		return ResponseEntity.ok(service.getTownListByCity(cityQuery));
	}
	
	
	/**
	 * 시도명 검색 (LIKE 기반)
	 *
	 * @param q     검색어
	 * @param limit 최대 반환 개수 (기본값 20)
	 * @return 검색된 시도명 목록
	 */
	@GetMapping("/search/province")
	public ResponseEntity<List<String>> searchProvince(
			@RequestParam("q") String q,
			@RequestParam(value = "limit", defaultValue = "20") int limit
	) {
		return ResponseEntity.ok(service.searchProvinces(q, limit));
	}
	
	/**
	 * 시군구명 검색 (LIKE 기반)
	 *
	 * @param q     검색어
	 * @param limit 최대 반환 개수 (기본값 20)
	 * @return 시군구명 목록
	 */
	@GetMapping("/search/city")
	public ResponseEntity<List<String>> searchCity(
			@RequestParam("q") String q,
			@RequestParam(value = "limit", defaultValue = "20") int limit
	) {
		return ResponseEntity.ok(service.searchCities(q, limit));
	}
	
	/**
	 * 읍면동/리 검색 (LIKE 기반)
	 *
	 * @param q     검색어
	 * @param limit 최대 반환 개수 (기본값 20)
	 * @return 읍면동/리 목록
	 */
	@GetMapping("/search/town")
	public ResponseEntity<List<String>> searchTown(
			@RequestParam("q") String q,
			@RequestParam(value = "limit", defaultValue = "20") int limit
	) {
		return ResponseEntity.ok(service.searchTowns(q, limit));
	}
}

