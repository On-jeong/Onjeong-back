package com.example.onjeong.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginDto {
    private String userNickname;
    private String userPassword;
}
