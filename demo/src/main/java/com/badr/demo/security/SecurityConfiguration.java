package com.badr.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder passwordEncoder= passwordEncoder();
        /*String encodedPassword = passwordEncoder.encode("1234");
        System.out.println(encodedPassword);
         auth.inMemoryAuthentication()
                 .withUser("user1").password(encodedPassword).roles("USER");
        auth.inMemoryAuthentication()
                .withUser("user2").password(passwordEncoder.encode("1111")).roles("USER");
        auth.inMemoryAuthentication()
                .withUser("admin").password(passwordEncoder.encode("1231")).roles("USER","ADMIN");*/
        auth.jdbcAuthentication()
                .dataSource(dataSource).usersByUsernameQuery("select username as principal, password as credentials, active from users where username=?")
                .authoritiesByUsernameQuery("select username as principal, role as role from users_roles where username=?")
                .rolePrefix("ROLE_")
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
         http.formLogin();
         http.authorizeHttpRequests().antMatchers("/").permitAll();
         http.authorizeHttpRequests().antMatchers("/admin/**").hasRole("ADMIN");
         http.authorizeHttpRequests().antMatchers("/user/**").hasRole("USER");
         http.authorizeHttpRequests().anyRequest().authenticated();
         http.exceptionHandling().accessDeniedPage("/403");
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
