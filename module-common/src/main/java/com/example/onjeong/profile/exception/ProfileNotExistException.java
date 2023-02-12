package com.example.onjeong.profile.exception;

import com.example.onjeong.error.ErrorCode;
import lombok.Getter;

@Getter
public class ProfileNotExistException extends RuntimeException{
    private final ErrorCode errorCode;

    public ProfileNotExistException(String message, ErrorCode errorCode){
        super(message);
        this.errorCode = errorCode;
    }
}
