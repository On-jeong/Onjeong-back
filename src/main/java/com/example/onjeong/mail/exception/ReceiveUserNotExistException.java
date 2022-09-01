package com.example.onjeong.mail.exception;

import com.example.onjeong.error.ErrorCode;
import lombok.Getter;

@Getter
public class ReceiveUserNotExistException extends RuntimeException{

    private final ErrorCode errorCode;

    public ReceiveUserNotExistException(String message, ErrorCode errorCode){
        super(message);
        this.errorCode = errorCode;
    }
}