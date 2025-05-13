package com.example.backend.service;

import com.example.backend.entity.CustomUserDetails;
import com.example.backend.entity.Users;
import com.example.backend.repository.UsersRepository;
import org.hibernate.Hibernate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UsersRepository usersRepository;

    public CustomUserDetailsService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Finding user with email: " + email);
        Users users = usersRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email " + email));
        System.out.println("Found user: " + users.getEmail());


        Hibernate.initialize(users.getUserRoles());
        if (users.getUserRoles() != null) {
            users.getUserRoles().forEach(userRole -> {
                Hibernate.initialize(userRole.getRole());

            });
        }

        return new CustomUserDetails(users);
    }
}