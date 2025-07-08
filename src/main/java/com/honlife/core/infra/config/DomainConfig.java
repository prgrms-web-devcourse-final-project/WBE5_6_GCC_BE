package com.honlife.core.infra.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("com.honlife.core")
@EnableJpaRepositories("com.honlife.core")
@EnableTransactionManagement
public class DomainConfig {
}
