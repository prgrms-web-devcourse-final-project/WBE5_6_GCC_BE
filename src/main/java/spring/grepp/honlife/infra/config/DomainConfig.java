package spring.grepp.honlife.infra.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("spring.grepp.honlife")
@EnableJpaRepositories("spring.grepp.honlife")
@EnableTransactionManagement
public class DomainConfig {
}
