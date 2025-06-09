package com.example.backend.service.Impl;

import com.example.backend.dto.request.UserManagementRequest;
import com.example.backend.dto.response.UserManagementResponse;
import com.example.backend.dto.response.UserResponse;
import com.example.backend.entity.CustomUserDetails;
import com.example.backend.entity.Role;
import com.example.backend.entity.UserRole;
import com.example.backend.entity.Users;
import com.example.backend.enums.RoleName;
import com.example.backend.exception.DuplicateResourceException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.mapper.UserManagementMapper;
import com.example.backend.mapper.UserMapper;
import com.example.backend.repository.RoleRepository;
import com.example.backend.repository.UsersRepository;
import com.example.backend.service.UsersService;
import com.example.backend.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {
    UsersRepository usersRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    UserManagementMapper userManagementMapper;
    JwtUtil jwtUtil;
    RoleRepository roleRepository;

    @Override
    public UserResponse findUserById(int id) {
        Users users = usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        return userMapper.toUserResponse(users);
    }
    @Transactional
    @Override
    public Map<String, String> changePassword(int id, String oldPassword, String newPassword) {
        Users users = usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        if (!passwordEncoder.matches(oldPassword, users.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }
        users.setPassword(passwordEncoder.encode(newPassword));
        usersRepository.save(users);
        return jwtUtil.generateTokens(new CustomUserDetails(users));
    }

    @Override
    public UserManagementResponse addUser(UserManagementRequest userManagementRequest) {
        if(usersRepository.existsByEmail(userManagementRequest.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        Users users = userManagementMapper.toUsers(userManagementRequest);
        users.setPassword(passwordEncoder.encode(userManagementRequest.getPassword()));
        String roleName = userManagementRequest.getRole();
        if (roleName != null && !roleName.isEmpty()) {
            Role role = roleRepository.findByRoleName(RoleName.valueOf(roleName.toUpperCase()))
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found with name: " + roleName));
            UserRole userRole = new UserRole();
            userRole.setRole(role);
            userRole.setUsers(users);
            List<UserRole> userRoles = new ArrayList<>();
            userRoles.add(userRole);
            users.setUserRoles(userRoles);
        }

        Users savedUser = usersRepository.save(users);
        return userManagementMapper.toUserManagementResponse(savedUser);
    }

    @Override
    public UserManagementResponse updateUser(int id, UserManagementRequest userManagementRequest) {
        Users users = usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));

        String email = userManagementRequest.getEmail();
        if (email != null && !email.isEmpty()) {
            Optional<Users> userWithEmail = usersRepository.findByEmail(email);
            if (userWithEmail.isPresent() && userWithEmail.get().getId() != id) {
                throw new DuplicateResourceException("Email already exists");
            }
        }

        users.setFirstName(userManagementRequest.getFirstName());
        users.setLastName(userManagementRequest.getLastName());
        users.setEmail(userManagementRequest.getEmail());
        String password = userManagementRequest.getPassword();
        password = passwordEncoder.encode(password);
        users.setPassword(password);
        users.setDepartment(userManagementRequest.getDepartment());

        String roleName = userManagementRequest.getRole();
        if (roleName != null && !roleName.isEmpty()) {
            // Xóa role cũ
            users.getUserRoles().clear();

            Role role = roleRepository.findByRoleName(RoleName.valueOf(roleName.toUpperCase()))
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found with name: " + roleName));

            UserRole userRole = new UserRole();
            userRole.setRole(role);
            userRole.setUsers(users);

            users.getUserRoles().add(userRole);
        }

        Users updatedUser = usersRepository.save(users);

        return userManagementMapper.toUserManagementResponse(updatedUser);
    }

    @Override
    public void deleteUser(int id) {
        usersRepository.deleteById(id);
    }

    @Override
    public Page<UserManagementResponse> getAllUsers(Pageable pageable) {
        Page<Users> getAll = usersRepository.findAll(pageable);
        return getAll.map(userManagementMapper::toUserManagementResponse);
    }

    @Override
    public Page<UserManagementResponse> searchUsersByName(String name, Pageable pageable) {
        Page<Users> usersPage = usersRepository.findByNameContainingIgnoreCase(name.trim(), pageable);
        return usersPage.map(userManagementMapper::toUserManagementResponse);
    }
}
