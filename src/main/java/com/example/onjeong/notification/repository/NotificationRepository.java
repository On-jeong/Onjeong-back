package com.example.onjeong.notification.repository;

import com.example.onjeong.notification.domain.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.*;
public interface NotificationRepository extends JpaRepository<Notifications, Long> {



}
