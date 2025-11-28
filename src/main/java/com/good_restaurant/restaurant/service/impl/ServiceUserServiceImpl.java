package com.good_restaurant.restaurant.service.impl;

import com.good_restaurant.restaurant.domain.ServiceUser;
import com.good_restaurant.restaurant.domain.UserRole;
import com.good_restaurant.restaurant.repository.ServiceUserRepository;
import com.good_restaurant.restaurant.service.A_Exception.MergePropertyException;
import com.good_restaurant.restaurant.service.A_base.BaseCRUD;
import com.good_restaurant.restaurant.service.A_base.JwtUtilities;
import com.good_restaurant.restaurant.service.A_base.ServiceHelper;
import com.good_restaurant.restaurant.service.ServiceUserService;
import com.good_restaurant.restaurant.service.UserRoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceUserServiceImpl implements ServiceUserService, BaseCRUD<ServiceUser, UUID> {

	private final ServiceUserRepository repository;
	private final ServiceHelper<ServiceUser, UUID> serviceHelper;
	private final UserRoleService userRoleService;
	private final JwtUtilities jwtUtilities;
	
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
	
	@SneakyThrows
	@Transactional
	public ServiceUser loadOrCreateUser(Jwt jwt) {
		
		UUID sub = UUID.fromString(jwt.getSubject());
		String email = jwt.getClaim("email");
		String name = jwt.getClaim("name");
		
		// roles 추출
		Set<String> roles = jwtUtilities.extractServiceRoles(jwt);
		
		// 기존 유저 조회
		ServiceUser user = repository.findById(sub)
				                   .orElseGet(() -> createNewUser(sub, email, name));
		
		// 기본 정보 업데이트
		updateUserFromToken(user, email, name);
		
		// 역할 동기화 (Keycloak → DB 반영)
		syncUserRoles(user, roles);
		
		return user;
	}
	
	private ServiceUser createNewUser(UUID sub, String email, String name) {
		ServiceUser newUser =
				ServiceUser.builder()
						.userId(sub)
						.email(email)
						.name(name)
						.lastLogin(LocalDateTime.now())
						.build();
		return save(newUser);
	}
	
	
	@Transactional
	public void syncUserRoles(ServiceUser user, Set<String> rolesFromJwt) {
		
		Set<String> existing = user.getRoles().stream()
				                       .map(UserRole::getRole)
				                       .collect(Collectors.toSet());
		
		// 1) JWT에 있는데 DB에 없는 역할 추가
		for (String role : rolesFromJwt) {
			if (!existing.contains(role)) {
				userRoleService.addRole(user, role);
			}
		}
		
		// 2) JWT에 없는 역할은 삭제(옵션)
		//    Keycloak에서 권한 회수되면 서비스에서도 회수해야 하는 경우
		for (String role : existing) {
			if (!rolesFromJwt.contains(role)) {
				userRoleService.removeRole(user, role);
			}
		}
	}
	
	
	@Transactional
	protected void updateUserFromToken(ServiceUser user, String email, String name) throws MergePropertyException {
		
		ServiceUser updated = user.toBuilder()
				                      .email(email)
				                      .name(name)
				                      .lastLogin(LocalDateTime.now())
				                      .build();
		
		// 기존 엔티티에 overwrite 하는 식으로 업데이트
		// target -> source
		updateRule(updated, user);
	}
}
