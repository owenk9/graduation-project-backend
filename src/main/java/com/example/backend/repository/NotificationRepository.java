package com.example.backend.repository;

import com.example.backend.entity.Notification;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    @Query("select n from Notification n join fetch n.users where n.isRead = false and n.users.id = :usersId order by n.id desc ")
    List<Notification> findByUserIdAndIsReadFalse(int usersId);
    @Transactional
    @Modifying
    @Query("update Notification n set n.isRead = true where n.id = :notificationId")
    void markAsRead(int notificationId);
    @Query("select n from Notification n join fetch n.users where n.users.id = :usersId")
    List<Notification> findByUserId(int usersId);
}
