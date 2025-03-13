package com.example.backend.service;

import com.example.backend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersService extends JpaRepository<Users, Integer> {
}
