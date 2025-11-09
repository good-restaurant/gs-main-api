package com.good_restaurant.restaurant.service.impl;

import com.good_restaurant.restaurant.domain.RestaurantPicture;
import com.good_restaurant.restaurant.repository.RestaurantPictureRepository;
import com.good_restaurant.restaurant.service.A_Exception.MergePropertyException;
import com.good_restaurant.restaurant.service.A_base.BaseCRUD;
import com.good_restaurant.restaurant.service.A_base.ServiceHelper;
import com.good_restaurant.restaurant.service.RestaurantPictureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantPictureServiceImpl implements RestaurantPictureService, BaseCRUD<RestaurantPicture, Long> {
	
	private final RestaurantPictureRepository repository;
	private final ServiceHelper<RestaurantPicture, Long> serviceHelper;
	
	
	@Override
	public RestaurantPicture updateRule(RestaurantPicture source, RestaurantPicture target) throws MergePropertyException {
		return serviceHelper.updateRule(source, target);
	}
	
	@Override
	public JpaRepository<RestaurantPicture, Long> getRepository() {
		return this.repository;
	}
	
	@Override
	public RestaurantPicture delete(Long aLong) {
		Optional<RestaurantPicture> optional = repository.findById(aLong);
		repository.deleteById(aLong);
		return optional.orElse(null);
	}
	
	@Override
	public Long getEntityId(RestaurantPicture entity) {
		return entity.getId();
	}
	
	@Override
	public RestaurantPicture getPicture(Long pictureId) {
		return getById(pictureId);
	}
}
