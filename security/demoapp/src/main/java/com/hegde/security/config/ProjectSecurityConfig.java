package com.hegde.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class ProjectSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .and()
                .httpBasic();
    }

}

/**
 * @Configuration
 *                public class ProjectSecurityConfig {
 * 
 * @Bean
 *       public SecurityFilterChain filterChain(HttpSecurity http) throws
 *       Exception {
 *       http
 *       .authorizeHttpRequests((authz) -> authz
 *       .anyRequest().authenticated()
 *       )
 *       .httpBasic(withDefaults());
 *       return http.build();
 *       }
 * 
 *       }
 */