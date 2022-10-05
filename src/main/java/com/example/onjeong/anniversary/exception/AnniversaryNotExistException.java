package com.example.onjeong.anniversary.exception;

import com.example.onjeong.error.ErrorCode;
import lombok.Getter;

@Getter
public class AnniversaryNotExistException extends RuntimeException{
    private final ErrorCode errorCode;

    public AnniversaryNotExistException(String message, ErrorCode errorCode){
        super(message);
        this.errorCode = errorCode;
    }
}
