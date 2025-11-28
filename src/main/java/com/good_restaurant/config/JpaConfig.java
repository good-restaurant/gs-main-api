package com.good_restaurant.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA 환경에서 created updated at 정보를 자동으로 넣기 위한 간섭 허용
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {
}
