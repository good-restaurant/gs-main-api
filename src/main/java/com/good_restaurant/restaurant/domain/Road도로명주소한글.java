package com.good_restaurant.restaurant.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Builder(toBuilder = true, builderClassName = "RoadNameKoreanBuilder", builderMethodName = "enBuilder")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA용
@Entity
@Table(name = "\"도로명주소_한글\"", schema = "good_restaurant", indexes = {
		@Index(name = "idx_도로명주소_시도_시군구", columnList = "시도명, 시군구명"),
		@Index(name = "idx_도로명주소_건물번호", columnList = "도로명, 건물본번, 건물부번"),
		@Index(name = "idx_도로명주소_도로명", columnList = "도로명")
})
public class Road도로명주소한글 {
	@Id
	@Size(max = 26)
	@Column(name = "\"도로명주소관리번호\"", nullable = false, length = 26)
	private String 도로명주소관리번호;
	
	@Size(max = 10)
	@Column(name = "\"법정동코드\"", length = 10)
	private String 법정동코드;
	
	@Size(max = 40)
	@Column(name = "\"시도명\"", length = 40)
	private String 시도명;
	
	@Size(max = 40)
	@Column(name = "\"시군구명\"", length = 40)
	private String 시군구명;
	
	@Size(max = 40)
	@Column(name = "\"법정읍면동명\"", length = 40)
	private String 법정읍면동명;
	
	@Size(max = 40)
	@Column(name = "\"법정리명\"", length = 40)
	private String 법정리명;
	
	@Column(name = "\"산여부\"", length = Integer.MAX_VALUE)
	private String 산여부;
	
	@Column(name = "\"지번본번\"")
	private Integer 지번본번;
	
	@Column(name = "\"지번부번\"")
	private Integer 지번부번;
	
	@Size(max = 12)
	@Column(name = "\"도로명코드\"", length = 12)
	private String 도로명코드;
	
	@Size(max = 80)
	@Column(name = "\"도로명\"", length = 80)
	private String 도로명;
	
	@Column(name = "\"지하여부\"", length = Integer.MAX_VALUE)
	private String 지하여부;
	
	@Column(name = "\"건물본번\"")
	private Integer 건물본번;
	
	@Column(name = "\"건물부번\"")
	private Integer 건물부번;
	
	@Size(max = 60)
	@Column(name = "\"행정동코드\"", length = 60)
	private String 행정동코드;
	
	@Size(max = 60)
	@Column(name = "\"행정동명\"", length = 60)
	private String 행정동명;
	
	@Size(max = 5)
	@Column(name = "\"기초구역번호\"", length = 5)
	private String 기초구역번호;
	
	@Size(max = 400)
	@Column(name = "\"이전도로명주소\"", length = 400)
	private String 이전도로명주소;
	
	@Size(max = 8)
	@Column(name = "\"효력발생일\"", length = 8)
	private String 효력발생일;
	
	@Column(name = "\"공동주택구분\"", length = Integer.MAX_VALUE)
	private String 공동주택구분;
	
	@Size(max = 2)
	@Column(name = "\"이동사유코드\"", length = 2)
	private String 이동사유코드;
	
	@Size(max = 400)
	@Column(name = "\"건축물대장건물명\"", length = 400)
	private String 건축물대장건물명;
	
	@Size(max = 400)
	@Column(name = "\"시군구용건물명\"", length = 400)
	private String 시군구용건물명;
	
	@Size(max = 200)
	@Column(name = "\"비고\"", length = 200)
	private String 비고;
	
}