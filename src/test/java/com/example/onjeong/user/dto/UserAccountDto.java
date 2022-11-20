package com.example.onjeong.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountDto {
    private String userName;
    private String userPassword;
    private String userStatus;
    private LocalDate userBirth;
}
