package com.good_restaurant.restaurant.service.impl;

import com.good_restaurant.restaurant.domain.RestaurantMenu;
import com.good_restaurant.restaurant.dto.UploadResult;
import com.good_restaurant.restaurant.repository.RestaurantMenuRepository;
import com.good_restaurant.restaurant.service.A_Exception.MergePropertyException;
import com.good_restaurant.restaurant.service.A_base.BaseCRUD;
import com.good_restaurant.restaurant.service.A_base.ServiceHelper;
import com.good_restaurant.restaurant.service.RestaurantMenuService;
import com.good_restaurant.restaurant.service.SignedUrlDownloadService;
import com.good_restaurant.restaurant.service.SignedUrlUploadService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
		
		RestaurantMenu pictureInfo = repository.findById(restaurantMenu.getId())
				                             .orElseThrow(() -> new IllegalArgumentException("해당 메뉴가 확인되지 않습니다."));
		
		// UploadService 내부에서 정규화 + 인코딩 + objectKey 생성 처리를 모두 수행해야 한다
		UploadResult result = uploadService.uploadResult(
				file.getOriginalFilename(),
				file.getContentType(),
				file.getBytes()
		);
		
		// UploadResult 에서 uuid + objectKey 모두 꺼내야 함
		RestaurantMenu updated = pictureInfo.toBuilder()
				                         .pictureUuid(result.uuid())
				                         .s3ObjectKey(result.objectKey())  // 반드시 저장
				                         .originalFilename(file.getOriginalFilename()) // DB에 원본 이름은 그대로 저장
				                         .build();
		
		return repository.save(updated);
	}
	
	
	@Override
	public Page<RestaurantMenu> getMenusAsPage(Pageable pageable) {
		return repository.findAll(pageable);
	}
	
	@Override
	public RestaurantMenu findMenuById(Long id) {
		return repository.findById(id).orElse(null);
	}
	
	public RestaurantMenu saveMenu(RestaurantMenu menu) {
		return save(menu);
	}
	
	@SneakyThrows
	@Override
	public RestaurantMenu updateMenu(Long id, RestaurantMenu newMenu) {
		return updateById(id, newMenu);
	}
}
