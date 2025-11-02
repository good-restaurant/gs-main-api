package com.good_restaurant.restaurant.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeocodeResultDto {
	private final BigDecimal lon;
	private final BigDecimal lat;
}
