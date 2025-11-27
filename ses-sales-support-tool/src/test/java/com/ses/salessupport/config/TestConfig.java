package com.ses.salessupport.config;

import java.util.Arrays;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * テスト用設定クラス
 */
@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public UserDetailsService testUserDetailsService() {
        UserDetails admin = User.builder()
                .username("admin@test.com")
                .password("password")
                .authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .build();

        UserDetails user = User.builder()
                .username("user@test.com")
                .password("password")
                .authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")))
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }
}

