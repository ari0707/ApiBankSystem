package com.ironhack.BankSystem.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);

    }
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
                        .requestMatchers(HttpMethod.GET, "/api/users/{username}").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/accounts").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")

                        .requestMatchers(HttpMethod.POST, "/api/users/new").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/accounts/**").hasRole("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/**").hasAuthority("ROLE_ADMIN")

                        .requestMatchers(HttpMethod.PATCH, "/api/users/edit/**}").hasRole("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/accounts/edit/**}").hasRole("ROLE_ADMIN")


                        .anyRequest()
                        .authenticated()
                )
                .httpBasic();

        return http.build();
    }

    }


