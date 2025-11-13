package com.good_restaurant.restaurant.service.impl;

import com.good_restaurant.restaurant.domain.Restaurant;
import com.good_restaurant.restaurant.domain.RestaurantMenu;
import com.good_restaurant.restaurant.domain.RestaurantPicture;
import com.good_restaurant.restaurant.dto.UploadResult;
import com.good_restaurant.restaurant.repository.RestaurantMenuRepository;
import com.good_restaurant.restaurant.service.A_Exception.MergePropertyException;
import com.good_restaurant.restaurant.service.A_base.BaseCRUD;
import com.good_restaurant.restaurant.service.A_base.ServiceHelper;
import com.good_restaurant.restaurant.service.RestaurantMenuService;
import com.good_restaurant.restaurant.service.RestaurantPictureService;
import com.good_restaurant.restaurant.service.SignedUrlDownloadService;
import com.good_restaurant.restaurant.service.SignedUrlUploadService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantMenuServiceImpl implements RestaurantMenuService, BaseCRUD<RestaurantMenu, Long> {
	
	private final RestaurantMenuRepository repository;
	private final ServiceHelper<RestaurantMenu, Long> serviceHelper;
	private final SignedUrlUploadService uploadService;
	private final SignedUrlDownloadService downloadService;
	
	@Override
	public RestaurantMenu updateRule(RestaurantMenu source, RestaurantMenu target) throws MergePropertyException {
		return serviceHelper.updateRule(source, target);
	}
	
	@Override
	public JpaRepository<RestaurantMenu, Long> getRepository() {
		return this.repository;
	}
	
	@Override
	public RestaurantMenu delete(Long aLong) {
		Optional<RestaurantMenu> optional = repository.findById(aLong);
		repository.deleteById(aLong);
		return optional.orElse(null);
	}
	
	@Override
	public Long getEntityId(RestaurantMenu entity) {
		return entity.getId();
	}
	
	@SneakyThrows
	@Override
	public RestaurantMenu uploadRestaurantPicture(RestaurantMenu restaurantMenu, MultipartFile file) {
		
		RestaurantMenu pictureInfo = repository.findById(restaurantMenu.getId()).orElseThrow(() -> new IllegalArgumentException("해당 메뉴가 확인되지 않습니다."));;
		
		UploadResult result = uploadService.uploadResult(
				file.getOriginalFilename(),
				file.getContentType(),
				file.getBytes()
		);
		
		// toBuilder 로 새로운 엔티티 생성
		RestaurantMenu updatedPicture = pictureInfo.toBuilder()
				                         .pictureUuid(result.uuid())
				                         .build();
		
		
		return repository.save(updatedPicture);
	}
}
