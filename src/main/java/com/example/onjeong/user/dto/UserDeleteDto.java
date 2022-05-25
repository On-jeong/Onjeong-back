package com.example.onjeong.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDeleteDto {
    private String userNickname;
    private String userPassword;
}
