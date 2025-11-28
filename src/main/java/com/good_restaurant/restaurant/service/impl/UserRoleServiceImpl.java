package com.good_restaurant.restaurant.service.impl;

import com.good_restaurant.restaurant.domain.ServiceUser;
import com.good_restaurant.restaurant.domain.UserRole;
import com.good_restaurant.restaurant.repository.UserRoleRepository;
import com.good_restaurant.restaurant.service.A_Exception.MergePropertyException;
import com.good_restaurant.restaurant.service.A_base.BaseCRUD;
import com.good_restaurant.restaurant.service.A_base.ServiceHelper;
import com.good_restaurant.restaurant.service.UserRoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService, BaseCRUD<UserRole, UUID> {
	
	private final UserRoleRepository repository;
	private final ServiceHelper<UserRole, UUID> serviceHelper;
	
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
	public UserRole updateRule(UserRole source, UserRole target) throws MergePropertyException {
		return serviceHelper.updateRule(source, target);
	}
	
	@Override
	public JpaRepository<UserRole, UUID> getRepository() {
		return this.repository;
	}
	
	@Override
	public UserRole delete(UUID uuid) {
		UserRole userRole = getById(uuid);
		repository.delete(userRole);
		return userRole;
	}
	
	@Override
	public UUID getEntityId(UserRole entity) {
		return entity.getId();
	}
	
	@Override
	@Transactional
	public UserRole addRole(ServiceUser user, String roleName) {
		
		user.addRole(roleName);
		
		// 변경사항 User 쪽 cascade ALL → 자동 반영됨
		// 또는 역할만 저장하고 싶으면 repository.save()
		return user.getRoles().stream()
				       .filter(r -> r.getRole().equals(roleName))
				       .findFirst()
				       .orElseThrow();
	}
	
	@Override
	@Transactional
	public UserRole removeRole(ServiceUser user, String roleName) {
		
		UserRole roleEntity = user.getRoles().stream()
				                      .filter(r -> r.getRole().equals(roleName))
				                      .findFirst()
				                      .orElse(null);
		
		if (roleEntity == null) {
			// 삭제할 역할이 없으면 null 또는 예외
			return null;
		}
		
		user.removeRole(roleEntity);
		
		// orphanRemoval=true
		return roleEntity;
	}
}
