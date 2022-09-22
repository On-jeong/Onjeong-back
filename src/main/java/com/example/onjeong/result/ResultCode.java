package com.example.onjeong.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {
    // User
    REGISTER_SUCCESS(200, "U001", "회원가입에 성공했습니다."),
    LOGIN_SUCCESS(200, "U002", "로그인에 성공했습니다.");


    private final int status;
    private final String code;
    private final String message;
}
