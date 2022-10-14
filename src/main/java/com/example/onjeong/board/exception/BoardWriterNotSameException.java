package com.example.onjeong.board.exception;

import com.example.onjeong.error.ErrorCode;
import lombok.Getter;

@Getter
public class BoardWriterNotSameException extends RuntimeException{
    private final ErrorCode errorCode;

    public BoardWriterNotSameException(String message, ErrorCode errorCode){
        super(message);
        this.errorCode = errorCode;
    }
}
