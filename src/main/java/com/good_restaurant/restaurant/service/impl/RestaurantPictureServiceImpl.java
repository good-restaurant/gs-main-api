package com.good_restaurant.restaurant.service.impl;

import com.good_restaurant.restaurant.domain.Restaurant;
import com.good_restaurant.restaurant.domain.RestaurantPicture;
import com.good_restaurant.restaurant.dto.UploadResult;
import com.good_restaurant.restaurant.repository.RestaurantPictureRepository;
import com.good_restaurant.restaurant.service.A_Exception.MergePropertyException;
import com.good_restaurant.restaurant.service.A_base.BaseCRUD;
import com.good_restaurant.restaurant.service.A_base.ServiceHelper;
import com.good_restaurant.restaurant.service.RestaurantPictureService;
import com.good_restaurant.restaurant.service.SignedUrlUploadService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantPictureServiceImpl implements RestaurantPictureService, BaseCRUD<RestaurantPicture, Long> {
	
	private final RestaurantPictureRepository repository;
	private final ServiceHelper<RestaurantPicture, Long> serviceHelper;
	private final SignedUrlUploadService uploadService;
	
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
	
	// 업로드 전체 흐름은 도메인 서비스가 담당
	@SneakyThrows
	@Transactional
	public RestaurantPicture uploadRestaurantPicture(Long restaurantId, MultipartFile file) {
	
//		파일 검증로직 따로
//		RestaurantPictureUploadValidator.verify(file);
		
		UploadResult result = uploadService.uploadResult(
				file.getOriginalFilename(),
				file.getContentType(),
				file.getBytes()
		);
		
		RestaurantPicture picture = RestaurantPicture.builder()
				                            .restaurant(Restaurant.builder().id(restaurantId).build())
				                            .pictureUuid(UUID.fromString(result.objectKey().split("_")[0]))
				                            .originalFilename(file.getOriginalFilename())
				                            .s3ObjectKey(result.objectKey())
				                            .build();
		
		return repository.save(picture);
	}
}
