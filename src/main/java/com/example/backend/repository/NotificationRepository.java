package com.example.backend.repository;

import com.example.backend.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    @Query("select n from Notification n join fetch n.users where n.isRead = false and n.users.id = :usersId")
    List<Notification> findByUserIdAndIsReadFalse(int usersId);
}
