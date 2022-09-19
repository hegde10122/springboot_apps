package com.hegde.security.config;

import org.apache.catalina.authenticator.SpnegoAuthenticator.AuthenticateAction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class ProjectSecurityConfig extends WebSecurityConfigurerAdapter {

    // AuthenticationManagerBuilder --- used for authentication
    @Override
    protected void configure(AuthenticationManagerBuilder authenticationBuilder) throws Exception {

        authenticationBuilder.inMemoryAuthentication()
                .withUser("vaibhav")
                .password("dongare")
                .roles("user")
                .and().withUser("ganesh").password("hegde").roles("admin");
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    // HttpSecurity --- used for authorization
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests()
                .antMatchers("/notices").permitAll() // using ant wildcards --- path paths are defined
                .antMatchers("/contact").hasAnyRole("user", "admin")
                .antMatchers("/myBalance").hasRole("admin")
                .antMatchers("/myAccount").authenticated()
                .antMatchers("/myLoans").authenticated()
                .antMatchers("/myCards").authenticated()
                .antMatchers("/home").authenticated()
                .antMatchers("/user").hasRole("user")
                .antMatchers("/admin").hasRole("admin")
                .antMatchers(method)
                .and()
                .formLogin() // all browser requests coming from forms -- login forms,contact forms etc
                .and()
                .httpBasic(); // all backend API and Postman requests
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