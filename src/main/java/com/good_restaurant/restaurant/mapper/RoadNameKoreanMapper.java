package com.good_restaurant.restaurant.mapper;

import com.good_restaurant.restaurant.domain.Road도로명주소한글;
import com.good_restaurant.restaurant.dto.RoadNameKoreanDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoadNameKoreanMapper {
	Road도로명주소한글 toEntity(RoadNameKoreanDto roadNameKoreanDto);
	
	RoadNameKoreanDto toDto(Road도로명주소한글 Road도로명주소한글);
	
	List<Road도로명주소한글> toEntity(List<RoadNameKoreanDto> roadNameKoreanDto);
	
	List<RoadNameKoreanDto> toDto(List<Road도로명주소한글> Road도로명주소한글);
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	Road도로명주소한글 partialUpdate(RoadNameKoreanDto roadNameKoreanDto, @MappingTarget Road도로명주소한글 Road도로명주소한글);
}