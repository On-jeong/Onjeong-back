package com.example.onjeong.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class UserDeleteDto {
    private String userPassword;
}