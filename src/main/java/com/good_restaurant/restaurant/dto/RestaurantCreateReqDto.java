package com.good_restaurant.restaurant.dto;

import com.good_restaurant.restaurant.domain.Category;
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

	// restaurant info
	@NotNull
	@Size(max = 100)
	private String restaurantName;
	@NotNull
	@Size(max = 255)
	private String address;
	@NotNull
	private Category category;  // TODO: NotNull 검토 필요

	@NotNull
	private BigDecimal lon;
	@NotNull
	private BigDecimal lat;

	@NotNull
	@Size(max = 50)
	private String ctpKorNm;
	@NotNull
	@Size(max = 50)
	private String sigKorNm;
	@NotNull
	@Size(max = 50)
	private String emdKorNm;
	// TODO: NotNull 검토 필요

	// restaurant_detail info
	@Size(max = 255)
	private String menu;
	@Size(max = 20)
	private String phoneNumber;
}