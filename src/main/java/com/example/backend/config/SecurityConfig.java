package com.example.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
//    private final JwtFilter jwtFilter;
//    public SecurityConfig(JwtFilter jwtFilter) {
//        this.jwtFilter = jwtFilter;
//    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll() // Đăng nhập và đăng ký

                        // Equipment endpoints
                        .requestMatchers("/equipment/add").hasAuthority("MANAGE_EQUIPMENT")
                        .requestMatchers("/equipment/get", "/equipment/get/**").hasAuthority("VIEW_EQUIPMENT")
                        .requestMatchers("/equipment/update/**").hasAuthority("MANAGE_EQUIPMENT")
                        .requestMatchers("/equipment/delete/**").hasAuthority("MANAGE_EQUIPMENT")

                        // Location endpoints
                        .requestMatchers("/location/add").hasAuthority("MANAGE_EQUIPMENT")
                        .requestMatchers("/location/get", "/location/get/**").hasAuthority("VIEW_EQUIPMENT")
                        .requestMatchers("/location/update/**").hasAuthority("MANAGE_EQUIPMENT")
                        .requestMatchers("/location/delete/**").hasAuthority("MANAGE_EQUIPMENT")

                        // Permission endpoints
                        .requestMatchers("/permission/add").hasAuthority("MANAGE_PERMISSION")
                        .requestMatchers("/permission/get", "/permission/get/**").hasAuthority("VIEW_PERMISSION")
                        .requestMatchers("/permission/update/**").hasAuthority("MANAGE_PERMISSION")
                        .requestMatchers("/permission/delete/**").hasAuthority("MANAGE_PERMISSION")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
