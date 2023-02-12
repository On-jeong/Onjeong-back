package com.example.onjeong.S3;

import com.example.onjeong.error.ErrorCode;
import lombok.Getter;

@Getter
public class FileUploadException extends RuntimeException{
    private final ErrorCode errorCode;

    public FileUploadException(String message, ErrorCode errorCode){
        super(message);
        this.errorCode = errorCode;
    }
}
