package com.example.onjeong.notification.domain;

import com.example.onjeong.Config.EmptyStringToNullConverter;
import com.example.onjeong.user.domain.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Notifications {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="notification_id", nullable = false, unique = true)
    private Long notificationId;

    @Convert(converter = EmptyStringToNullConverter.class)
    @Column(name="notification_content", nullable = false)
    private String notificationContent;

    @Column(name="notification_time", nullable = false)
    private LocalDateTime notificationTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}