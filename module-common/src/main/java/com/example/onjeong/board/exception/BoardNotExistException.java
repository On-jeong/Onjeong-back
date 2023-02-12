package com.example.onjeong.board.exception;

import com.example.onjeong.error.ErrorCode;
import lombok.Getter;

@Getter
public class BoardNotExistException extends RuntimeException{
    private final ErrorCode errorCode;

    public BoardNotExistException(String message, ErrorCode errorCode){
        super(message);
        this.errorCode = errorCode;
    }
}
