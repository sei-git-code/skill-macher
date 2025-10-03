package com.ses.salessupport.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.ses.salessupport.repository")
@EnableTransactionManagement
public class DatabaseConfig {
}
