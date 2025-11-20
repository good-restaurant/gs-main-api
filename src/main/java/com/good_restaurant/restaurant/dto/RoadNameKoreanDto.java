package com.good_restaurant.restaurant.dto;

import com.good_restaurant.restaurant.domain.Road도로명주소한글;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link Road도로명주소한글}
 */
public record RoadNameKoreanDto(@Size(max = 40) String 시도명, @Size(max = 40) String 시군구명, @Size(max = 40) String 법정읍면동명,
                                @Size(max = 40) String 법정리명, Integer 지번본번, Integer 지번부번, @Size(max = 80) String 도로명,
                                Integer 건물본번, Integer 건물부번, @Size(max = 60) String 행정동명) implements Serializable {
}