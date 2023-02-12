package com.example.onjeong.question.exception;

import com.example.onjeong.error.ErrorCode;
import lombok.Getter;

@Getter
public class AnswerNotExistException extends RuntimeException{

    private final ErrorCode errorCode;

    public AnswerNotExistException(String message, ErrorCode errorCode){
        super(message);
        this.errorCode = errorCode;
    }
}