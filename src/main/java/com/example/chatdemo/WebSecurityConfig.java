package com.example.chatdemo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.LinkedList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                //.antMatchers("/", "/home").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails thai = User.withDefaultPasswordEncoder()
            .username("thai")
            .password("thai1")
            .roles("USER")
            .build();
        UserDetails sarah = User.withDefaultPasswordEncoder()
                .username("sarah")
                .password("sarah1")
                .roles("USER")
                .build();
        UserDetails jose = User.withDefaultPasswordEncoder()
                .username("jose")
                .password("jose1")
                .roles("USER")
                .build();
        List<UserDetails> users = new LinkedList<>();
        users.add(thai);
        users.add(sarah);
        users.add(jose);

        return new InMemoryUserDetailsManager(users);
    }
}
