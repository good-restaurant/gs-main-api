package com.good_restaurant.restaurant.service.impl;

import com.good_restaurant.restaurant.domain.Road도로명주소한글;
import com.good_restaurant.restaurant.repository.RoadNameKoreanRepository;
import com.good_restaurant.restaurant.service.A_Exception.MergePropertyException;
import com.good_restaurant.restaurant.service.A_base.BaseCRUD;
import com.good_restaurant.restaurant.service.A_base.ServiceHelper;
import com.good_restaurant.restaurant.service.RoadNameKoreanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoadNameKoreanServiceImpl implements RoadNameKoreanService, BaseCRUD<Road도로명주소한글, String> {
	
	private final RoadNameKoreanRepository repository;
	private final ServiceHelper<Road도로명주소한글, String> serviceHelper;
	
	@Override
	public Road도로명주소한글 updateRule(Road도로명주소한글 source, Road도로명주소한글 target) throws MergePropertyException {
		return serviceHelper.updateRule(source, target);
	}
	
	@Override
	public JpaRepository<Road도로명주소한글, String> getRepository() {
		return this.repository;
	}
	
	@Override
	public Road도로명주소한글 delete(String s) {
//		삭제로직 없음.
		return null;
	}
	
	@Override
	public String getEntityId(Road도로명주소한글 entity) {
		return entity.get도로명주소관리번호();
	}
	
	
	// 검색 + 후보 반환
	public Road도로명주소한글 searchCandidates(String query) {
		// 1. 도로명 기반 우선 검색
		List<Road도로명주소한글> primary =
				repository.findTop10By도로명ContainingOrderBy도로명Asc(query);
		
		// 2. 시도/시군구 포함한 wide 검색
		List<Road도로명주소한글> wide =
				repository.searchWide(query, PageRequest.of(0, 20));
		
		// 3. 후보 병합 (중복 제거)
		List<Road도로명주소한글> merged = Stream.concat(primary.stream(), wide.stream())
				                       .collect(Collectors.collectingAndThen(
						                       Collectors.toMap(Road도로명주소한글::get도로명주소관리번호, d -> d, (a, b) -> a),
						                       m -> new ArrayList<>(m.values())
				                       ));
		
		// 4. DTO 변환 + 정렬 (간단히 roadName 기준)
		return merged.getFirst();
	}
	
	// 완성형 풀주소 조립
	public String buildFullAddress(Road도로명주소한글 d) {
		StringBuilder sb = new StringBuilder();
		sb.append(d.get시도명()).append(" ");
		sb.append(d.get시군구명()).append(" ");
		sb.append(d.get도로명()).append(" ");
		sb.append(d.get건물본번());
		
		if (d.get건물부번() != null && d.get건물부번() > 0) {
			sb.append("-").append(d.get건물부번());
		}
		
		return sb.toString().trim();
	}
}
