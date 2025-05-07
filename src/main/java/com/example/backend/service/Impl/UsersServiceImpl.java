package com.example.backend.service.Impl;

import com.example.backend.dto.response.UserResponse;
import com.example.backend.entity.CustomUserDetails;
import com.example.backend.entity.Users;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.mapper.UserMapper;
import com.example.backend.repository.UsersRepository;
import com.example.backend.service.UsersService;
import com.example.backend.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {
    UsersRepository usersRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    JwtUtil jwtUtil;

    @Override
    public UserResponse findUserById(int id) {
        Users users = usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        return userMapper.toUserResponse(users);
    }
    @Transactional
    @Override
    public String changePassword(int id, String oldPassword, String newPassword) {
        Users users = usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        if (!passwordEncoder.matches(oldPassword, users.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }
        users.setPassword(passwordEncoder.encode(newPassword));
        usersRepository.save(users);
        return jwtUtil.generateToken(new CustomUserDetails(users));
    }
}
