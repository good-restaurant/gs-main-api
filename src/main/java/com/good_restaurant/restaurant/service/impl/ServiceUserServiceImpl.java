package com.good_restaurant.restaurant.service.impl;

import com.good_restaurant.restaurant.domain.ServiceUser;
import com.good_restaurant.restaurant.repository.ServiceUserRepository;
import com.good_restaurant.restaurant.service.A_Exception.MergePropertyException;
import com.good_restaurant.restaurant.service.A_base.BaseCRUD;
import com.good_restaurant.restaurant.service.A_base.ServiceHelper;
import com.good_restaurant.restaurant.service.ServiceUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServiceUserServiceImpl implements ServiceUserService, BaseCRUD<ServiceUser, UUID> {

	private final ServiceUserRepository repository;
	private final ServiceHelper<ServiceUser, UUID> serviceHelper;
	
	
	/**
	 * 소스 객체의 필드 값을 타겟 객체에 병합합니다.
	 * 타겟 객체의 필드 값이 null 이고 소스 객체의 필드 값이 null 이 아닌 경우에만 업데이트합니다.
	 * 고려사항 몇 개
	 * 1. 인터페이스 내에 있는 default 로직으로 인해 public 으로 할 수 밖에 없지만,
	 * 실제 사용은 protected 처럼 내부 메소드로 사용하세요.
	 * 2. 이 말은 컨트롤러가 직접 "updateRule" 을 사용하면 안된다는 의미입니다.
	 * 보통 update 를 하면 자연스럽게 호출되기 때문에 큰 문제가 안될 겁니다.
	 *
	 * @param source 소스 객체
	 * @param target 타겟 객체
	 * @return 업데이트된 타겟 객체
	 * @throws MergePropertyException 병합 과정에서 발생한 예외
	 */
	@Override
	public ServiceUser updateRule(ServiceUser source, ServiceUser target) throws MergePropertyException {
		return serviceHelper.updateRule(source, target);
	}
	
	@Override
	public JpaRepository<ServiceUser, UUID> getRepository() {
		return this.repository;
	}
	
	@Override
	public ServiceUser delete(UUID uuid) {
		ServiceUser user = getById(uuid);
		repository.delete(user);
		return user;
	}
	
	@Override
	public UUID getEntityId(ServiceUser entity) {
		return entity.getUserId();
	}
}
