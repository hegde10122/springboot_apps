package com.hegde.security.config;

import org.apache.catalina.authenticator.SpnegoAuthenticator.AuthenticateAction;
import org.springframework.beans.factory.annotation.Autowired;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class ProjectSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    private ActiveDirectoryWsdlConnector authProviderActiveDirectoryWsdlConnector;

    private static final String USERNAME = "vaibhav";
    private static final String USER_PWD = "dongare";
    private static final String ADMIN_NAME = "ganesh";
    private static final String ADMIN_PWD = "hegde";
    private static final String[] ROLES = { "ADMIN", "USER" };

    // AuthenticationManagerBuilder --- used for authentication --- core object for
    // authentication configuration.
    @Override
    protected void configure(AuthenticationManagerBuilder authenticationBuilder) throws Exception {

        // authenticationBuilder.userDetailsService(userDetailsService)

        authenticationBuilder.authenticationProvider(authProviderActiveDirectoryWsdlConnector);

        // authenticationBuilder.jdbcAuthentication().dataSource(dataSource);

        // authenticationBuilder.l

        // to override the above schemas
        /*
         * .authoritiesByUsernameQuery("select username,authority from authorities where username =?"
         * )
         * .usersByUsernameQuery("select username,password,enabled from users where username =?"
         * );
         */

        /*
         * authenticationBuilder.inMemoryAuthentication()
         * .withUser(USERNAME)
         * .password(USER_PWD)
         * .roles(ROLES[1])
         * .and().withUser(ADMIN_NAME).password(ADMIN_PWD).roles(ROLES[0]);
         */

        // ---- In normal world use-cases we do not have default schema and user roles
        // embedded in the code this way----this defaultschema is for H2 database -- the
        // in-memory database

        /*
         * authenticationBuilder.jdbcAuthentication()
         * .dataSource(dataSource)
         * .withDefaultSchema()
         * .withUser(User.withUsername(USERNAME).password(USER_PWD).roles(ROLES[1]))
         * .withUser(User.withUsername(ADMIN_NAME).password(ADMIN_PWD).roles(ROLES[0]));
         */

    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    // HttpSecurity --- used for authorization configuration
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests()
                .antMatchers("/notices").permitAll() // using ant wildcards --- path paths are defined
                .antMatchers("/contact").hasAnyRole(ROLES[0], ROLES[1])
                .antMatchers("/myBalance").hasRole(ROLES[0])
                .antMatchers("/myAccount").authenticated()
                .antMatchers("/myLoans").authenticated()
                .antMatchers("/myCards").authenticated()
                .antMatchers("/home").authenticated()
                .antMatchers("/user").hasRole(ROLES[1])
                .antMatchers("/admin").hasRole(ROLES[0])
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