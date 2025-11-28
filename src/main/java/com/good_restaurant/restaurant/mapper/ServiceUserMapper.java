package com.good_restaurant.restaurant.mapper;

import com.good_restaurant.restaurant.domain.ServiceUser;
import com.good_restaurant.restaurant.dto.ServiceUserDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ServiceUserMapper {
	ServiceUser toEntity(ServiceUserDto serviceUserDto);
	
	@AfterMapping
	default void linkRoles(@MappingTarget ServiceUser serviceUser) {
		serviceUser.getRoles().forEach(serviceUser::addRole);
	}
	
	ServiceUserDto toDto(ServiceUser serviceUser);
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	ServiceUser partialUpdate(ServiceUserDto serviceUserDto, @MappingTarget ServiceUser serviceUser);
}