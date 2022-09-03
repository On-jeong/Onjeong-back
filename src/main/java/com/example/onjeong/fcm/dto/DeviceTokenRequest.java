package com.example.onjeong.fcm.dto;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceTokenRequest {
    private String userNickname;
    private String token;
}