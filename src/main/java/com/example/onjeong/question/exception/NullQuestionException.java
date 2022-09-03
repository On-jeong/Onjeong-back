package com.example.onjeong.question.exception;

import com.example.onjeong.error.ErrorCode;
import lombok.Getter;

@Getter
public class NullQuestionException extends RuntimeException{

    private final ErrorCode errorCode;

    public NullQuestionException(String message, ErrorCode errorCode){
        super(message);
        this.errorCode = errorCode;
    }
}