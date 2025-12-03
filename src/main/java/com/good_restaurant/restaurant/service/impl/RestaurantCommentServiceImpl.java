package com.good_restaurant.restaurant.service.impl;

import com.good_restaurant.restaurant.domain.RestaurantComment;
import com.good_restaurant.restaurant.domain.RestaurantCommentFullDto;
import com.good_restaurant.restaurant.domain.RestaurantMenu;
import com.good_restaurant.restaurant.repository.RestaurantCommentRepository;
import com.good_restaurant.restaurant.service.A_Exception.MergePropertyException;
import com.good_restaurant.restaurant.service.A_base.BaseCRUD;
import com.good_restaurant.restaurant.service.A_base.ServiceHelper;
import com.good_restaurant.restaurant.service.RestaurantCommentService;
import com.good_restaurant.restaurant.service.SignedUrlUploadService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantCommentServiceImpl implements RestaurantCommentService, BaseCRUD<RestaurantComment, Long> {
	
	private final RestaurantCommentRepository repository;
	private final ServiceHelper<RestaurantComment, Long> serviceHelper;
	private final SignedUrlUploadService uploadService;
	
	@Override
	public RestaurantComment updateRule(RestaurantComment source, RestaurantComment target) throws MergePropertyException {
		return serviceHelper.updateRule(source, target);
	}
	
	@Override
	public JpaRepository<RestaurantComment, Long> getRepository() {
		return this.repository;
	}
	
	@Override
	public RestaurantComment delete(Long aLong) {
		Optional<RestaurantComment> optional = repository.findById(aLong);
		repository.deleteById(aLong);
		return optional.orElse(null);
	}
	
	@Override
	public Page<RestaurantComment> getRecentComments(Pageable pageable) {
		Pageable sorted = PageRequest.of(
				pageable.getPageNumber(),
				pageable.getPageSize(),
				Sort.by(
						Sort.Order.desc("timeRecord.updatedAt") // 최근 갱신
								.nullsLast() // null 은 맨 마지막에
				)
		);
		
		return repository.findAll(sorted);
	}
	
	@Override
	public Long getEntityId(RestaurantComment entity) {
		return entity.getId();
	}
	
	// 특정 식당의 코멘트 목록 (Pageable)
	@Override
	public List<RestaurantComment> getCommentsByRestaurantId(Long restaurantId, Pageable pageable) {
		return repository.findByRestaurant_Id(restaurantId, pageable).getContent();
	}
	
	// 코멘트 단일 조회
	@Override
	public RestaurantComment getCommentById(Long commentId) {
		return repository.findById(commentId)
				       .orElseThrow(() -> new IllegalArgumentException("Comment not found: " + commentId));
	}
	
	@Override
	public RestaurantComment createComment(RestaurantComment entity) {
		return save(entity);
	}
	
	@SneakyThrows
	@Override
	public RestaurantComment updateComment(Long id, RestaurantComment entity) {
		return update(entity, id);
	}
	
}
