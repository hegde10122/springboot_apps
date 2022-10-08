package com.hegde.security.config;

import org.apache.catalina.authenticator.SpnegoAuthenticator.AuthenticateAction;
import org.springframework.beans.factory.annotation.Autowired;

import javax.naming.ldap.LdapContext;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.ldap.repository.config.EnableLdapRepositories;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class ProjectSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    // @Autowired
    // UserDetailsService userDetailsService;

    // @Autowired
    // private ActiveDirectoryWsdlConnector
    // authProviderActiveDirectoryWsdlConnector;

    private static final String USERNAME = "vaibhav";
    private static final String USER_PWD = "dongare";
    private static final String ADMIN_NAME = "ganesh";
    private static final String ADMIN_PWD = "hegde";
    private static final String[] ROLES = { "ADMIN", "USER" };

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
                .antMatchers("/myBalance").authenticated()
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

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /*
         * auth.inMemoryAuthentication().withUser("admin").password("password").
         * authorities("admin").and().withUser("user")
         * .password("user").authorities("read").and().passwordEncoder(
         * NoOpPasswordEncoder.getInstance());
         */

        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();
        UserDetails user1 = User.withUsername("admin").password("password").authorities("admin").build();
        UserDetails user2 = User.withUsername("user").password("user").authorities("read").build();

        userDetailsManager.createUser(user2);
        userDetailsManager.createUser(user1);

        auth.userDetailsService(userDetailsManager);

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