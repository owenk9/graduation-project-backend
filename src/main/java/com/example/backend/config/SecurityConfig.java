package com.example.backend.config;

import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@KeycloakConfiguration
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(simpleAuthorityMapper());
        auth.authenticationProvider(keycloakAuthenticationProvider);
    }
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new NullAuthenticatedSessionStrategy();
    }

    @Override
    public void init(WebSecurity builder) throws Exception {

    }

    @Override
    public void configure(WebSecurity builder) throws Exception {

    }

    @Bean
    public SimpleAuthorityMapper simpleAuthorityMapper() {
        SimpleAuthorityMapper mapper = new SimpleAuthorityMapper();
        mapper.setPrefix("ROLE_");
        mapper.setConvertToUpperCase(true);
        return mapper;
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/location/add").hasRole("ADMIN")
                        .requestMatchers("/equipment/add").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/permission/add").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                );
    }
}
