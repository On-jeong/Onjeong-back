package com.example.onjeong.user.redis;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RequiredArgsConstructor
@RedisHash(value = "refreshToken", timeToLive = 50400)
public class RefreshToken {

    @Id
    private final String refreshToken;

    private final Long userId;

}