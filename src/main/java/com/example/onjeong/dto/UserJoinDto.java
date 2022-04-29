package com.example.onjeong.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
@NoArgsConstructor
public class UserJoinDto {
    private String userName;
    private String userNickname;
    private String userPassword;
    private String userStatus;
    private Date userBirth;
}
