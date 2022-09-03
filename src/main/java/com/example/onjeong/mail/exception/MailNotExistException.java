package com.example.onjeong.mail.exception;

import com.example.onjeong.error.ErrorCode;
import lombok.Getter;

@Getter
public class MailNotExistException extends RuntimeException{

    private final ErrorCode errorCode;

    public MailNotExistException(String message, ErrorCode errorCode){
        super(message);
        this.errorCode = errorCode;
    }
}