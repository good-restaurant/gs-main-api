package com.good_restaurant.restaurant.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Value;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder    // TODO: entity 생성을 위한 빌더로, 다시 고려해볼 필요 있음
public class RestaurantCreateReqDto {

	@NotNull
	@Size(max = 100)
	String restaurantName;
	@NotNull
	@Size(max = 255)
	String address;
	@NotNull
	@Size(max = 255)
	String category;
	@Size(max = 255)
	String menu;
	@Size(max = 20)
	String phoneNumber;

	@NotNull
	BigDecimal lon;
	@NotNull
	BigDecimal lat;

	@NotNull
	@Size(max = 50)
	String ctpKorNm;
	@NotNull
	@Size(max = 50)
	String sigKorNm;
	@NotNull
	@Size(max = 50)
	String emdKorNm;
}