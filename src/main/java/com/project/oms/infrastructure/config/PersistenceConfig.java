package com.project.oms.infrastructure.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.project.oms")
@EntityScan(basePackages = "com.project.oms")
public class PersistenceConfig {
}