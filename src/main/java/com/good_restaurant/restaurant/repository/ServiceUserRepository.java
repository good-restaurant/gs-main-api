package com.good_restaurant.restaurant.repository;

import com.good_restaurant.restaurant.domain.ServiceUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ServiceUserRepository extends JpaRepository<ServiceUser, UUID> {
}