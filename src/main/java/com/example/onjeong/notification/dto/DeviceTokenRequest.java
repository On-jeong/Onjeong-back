package com.example.onjeong.notification.dto;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceTokenRequest {
    private String userNickname;
    private String token;
}