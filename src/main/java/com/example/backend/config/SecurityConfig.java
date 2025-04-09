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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Thêm CORS vào đây
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/home/**").permitAll()
//                        .requestMatchers("/equipment/add").hasAuthority("MANAGE_EQUIPMENT")
                                .requestMatchers("/equipment/**").permitAll()
//                        .requestMatchers("/equipment/get", "/equipment/get/**").hasAuthority("VIEW_EQUIPMENT")
                                .requestMatchers("/equipment/get", "/equipment/get/**").permitAll()
//                                .requestMatchers("/equipment/update/**").hasAuthority("MANAGE_EQUIPMENT")
                                .requestMatchers("/equipment/update/**").permitAll()
//                        .requestMatchers("/equipment/delete/**").hasAuthority("MANAGE_EQUIPMENT")
                                .requestMatchers("/equipment/delete/**").permitAll()
//                                .requestMatchers("/location/add").hasAuthority("MANAGE_EQUIPMENT")
                                .requestMatchers("/location/**").permitAll()
//                        .requestMatchers("/location/get", "/location/get/**").hasAuthority("VIEW_EQUIPMENT")
//                        .requestMatchers("/location/update/**").hasAuthority("MANAGE_EQUIPMENT")
//                        .requestMatchers("/location/delete/**").hasAuthority("MANAGE_EQUIPMENT")
                                .requestMatchers("/permission/**").permitAll()
//                        .requestMatchers("/permission/add").hasAuthority("MANAGE_PERMISSION")
//                        .requestMatchers("/permission/get", "/permission/get/**").hasAuthority("VIEW_PERMISSION")
//                        .requestMatchers("/permission/update/**").hasAuthority("MANAGE_PERMISSION")
//                        .requestMatchers("/permission/delete/**").hasAuthority("MANAGE_PERMISSION")
                                .requestMatchers("/maintenance/**").permitAll()
                                .requestMatchers("/user/**").permitAll()
                                .requestMatchers("/borrowing/**").permitAll()
                                .requestMatchers("/home/**").permitAll()
                                .requestMatchers("/files/**").permitAll()
                                .requestMatchers("/category/**").permitAll()
                                .requestMatchers("/location/**").permitAll()
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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); // Cho phép React frontend
        configuration.setAllowedMethods(List.of("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true); // Cho phép gửi cookie nếu có

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
