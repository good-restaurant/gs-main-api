package com.good_restaurant.restaurant.service.impl;

import com.good_restaurant.restaurant.domain.Road도로명주소한글;
import com.good_restaurant.restaurant.repository.RoadNameKoreanRepository;
import com.good_restaurant.restaurant.service.A_Exception.MergePropertyException;
import com.good_restaurant.restaurant.service.A_base.BaseCRUD;
import com.good_restaurant.restaurant.service.A_base.ServiceHelper;
import com.good_restaurant.restaurant.service.RoadNameKoreanService;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
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
		
		log.info("wide size: {}", wide.size());
		log.debug("wide-search-result count: {}", wide.size());
		wide.forEach(r -> {
			log.debug("{} {} {} {}-{}",
					r.get시도명(),
					r.get시군구명(),
					r.get도로명(),
					r.get건물본번(),
					r.get건물부번()
			);
		});
		
		
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
	
	
	// 가나다순 정렬 유틸
	private static Comparator<String> STRING_ASC = Comparator.nullsLast(String::compareToIgnoreCase);
	
	@Override
	public List<String> getProvinceList() {
		return repository.findAllDistinctBy시도명().stream()
				       .sorted(STRING_ASC)
				       .collect(Collectors.toList());
	}
	
	@Override
	public List<String> getCityList() {
		return repository.findAllDistinctBy시군구명().stream()
				       .sorted(STRING_ASC)
				       .collect(Collectors.toList());
	}
	
	@Override
	public List<String> getTownList() {
		List<Tuple> rows = repository.findAllDistinctBy법정읍면동리명();
		
		List<String> result = new ArrayList<>();
		
		for (Tuple t : rows) {
			String 읍면동 = t.get(0, String.class);
			String 리 = t.get(1, String.class);
			
			if (리 == null || 리.isBlank()) {
				// 리가 없는 경우: 읍면동 단독
				result.add(읍면동);
			} else {
				// 리가 있는 경우: 읍면동 + 리
				result.add(리);
			}
		}
		
		return result.stream()
				       .distinct()
				       .sorted(String::compareToIgnoreCase)
				       .collect(Collectors.toList());
	}
	
	
	@Override
	public List<String> searchProvinces(String query, int limit) {
		String q = query.trim();
		
		return getRepository().findAll().stream()
				       .map(Road도로명주소한글::get시도명)
				       .filter(Objects::nonNull)
				       .filter(p -> p.contains(q))
				       .distinct()
				       .sorted(String::compareToIgnoreCase)
				       .limit(limit)
				       .collect(Collectors.toList());
	}
	
	@Override
	public List<String> searchCities(String query, int limit) {
		String q = query.trim();
		
		return getRepository().findAll().stream()
				       .map(r -> r.get시군구명())
				       .filter(Objects::nonNull)
				       .filter(c -> c.contains(q))
				       .distinct()
				       .sorted(String::compareToIgnoreCase)
				       .limit(limit)
				       .collect(Collectors.toList());
	}
	
	@Override
	public List<String> searchTowns(String query, int limit) {
		String q = query.trim();
		
		return getRepository().findAll().stream()
				       .map(r -> {
					       // 동 + 리 조립
					       if (r.get법정리명() != null && !r.get법정리명().isEmpty()) {
						       return r.get법정읍면동명() + " " + r.get법정리명();
					       }
					       return r.get법정읍면동명();
				       })
				       .filter(Objects::nonNull)
				       .filter(t -> t.contains(q))
				       .distinct()
				       .sorted(String::compareToIgnoreCase)
				       .limit(limit)
				       .collect(Collectors.toList());
	}
	
}
