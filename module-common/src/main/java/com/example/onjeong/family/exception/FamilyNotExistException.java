package com.example.onjeong.family.exception;

import com.example.onjeong.error.ErrorCode;
import lombok.Getter;

@Getter
public class FamilyNotExistException extends RuntimeException{
    private final ErrorCode errorCode;

    public FamilyNotExistException(String message, ErrorCode errorCode){
        super(message);
        this.errorCode = errorCode;
    }
}
