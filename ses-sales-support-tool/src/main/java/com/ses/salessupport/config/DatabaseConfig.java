package com.ses.salessupport.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.ses.salessupport.repository")
@EnableTransactionManagement
public class DatabaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    @Bean
    public CommandLineRunner databaseConnectionTest(JdbcTemplate jdbcTemplate) {
        return args -> {
            try {
                // データベース接続テスト
                String result = jdbcTemplate.queryForObject("SELECT 'Database connection successful' as message", String.class);
                logger.info("データベース接続テスト: {}", result);
                
                // テーブル存在確認
                String tableCount = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public'", 
                    String.class
                );
                logger.info("データベース内のテーブル数: {}", tableCount);
                
            } catch (Exception e) {
                logger.error("データベース接続エラー: {}", e.getMessage());
                throw e;
            }
        };
    }
}
