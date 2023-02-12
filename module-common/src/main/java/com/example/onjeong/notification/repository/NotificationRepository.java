package com.example.onjeong.notification.repository;

import com.example.onjeong.notification.domain.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notifications, Long> {

    @Query(nativeQuery = true,
            value="SELECT * FROM notifications n "+
                    "WHERE n.notification_time between date_sub(now(),INTERVAL 3 DAY) and now()" +
                    "AND n.user_id = :userId ORDER BY n.notification_id DESC")
    List<Notifications> findAllByUserId(@Param("userId") Long userId);
}
