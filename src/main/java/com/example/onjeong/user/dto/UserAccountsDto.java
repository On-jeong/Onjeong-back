package com.example.onjeong.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class UserAccountsDto {
    private String userName;
    private String userPassword;
    private String userStatus;
    private LocalDate userBirth;
}