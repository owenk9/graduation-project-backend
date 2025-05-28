package com.example.backend.service;

import com.example.backend.entity.CustomUserDetails;
import com.example.backend.entity.Users;
import com.example.backend.repository.UsersRepository;
import lombok.Getter;
import org.hibernate.Hibernate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UsersRepository usersRepository;
    @Getter
    private final EmailService emailService;
    public CustomUserDetailsService(UsersRepository usersRepository, EmailService emailService) {
        this.usersRepository = usersRepository;
        this.emailService = emailService;
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

    public void updateUser(Users user) {
        usersRepository.save(user); // Cập nhật mật khẩu (giả sử đã mã hóa bằng PasswordEncoder)
    }
}