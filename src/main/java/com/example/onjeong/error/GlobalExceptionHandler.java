package com.example.onjeong.error;

import com.example.onjeong.anniversary.exception.EmailDuplicateException;
import com.example.onjeong.mail.exception.MailNotExistException;
import com.example.onjeong.mail.exception.ReceiveUserNotExistException;
import com.example.onjeong.user.exception.UserNotExistException;
import com.example.onjeong.question.exception.AnswerNotExistException;
import com.example.onjeong.question.exception.NullQuestionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailDuplicateException.class)
    public ResponseEntity<ErrorResponse> handleEmailDuplicateException(EmailDuplicateException ex){
        log.error("handleEmailDuplicateException",ex);
        final ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler(UserNotExistException.class)
    public ResponseEntity<ErrorResponse> handleUserNotExistException(UserNotExistException ex){
        log.error("handleUserNotExistException",ex);
        final ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler(ReceiveUserNotExistException.class)
    public ResponseEntity<ErrorResponse> handleReceiveUserNotExistException(ReceiveUserNotExistException ex){
        log.error("handleReceiveUserNotExistException",ex);
        final ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler(MailNotExistException.class)
    public ResponseEntity<ErrorResponse> handleMailNotExistException(MailNotExistException ex){
        log.error("handleMailNotExistException",ex);
        final ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler(NullQuestionException.class)
    public ResponseEntity<ErrorResponse> handleNullQuestionException(NullQuestionException ex){
        log.error("handleNullQuestionException",ex);
        final ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler(AnswerNotExistException.class)
    public ResponseEntity<ErrorResponse> handleAnswerNotExistException(AnswerNotExistException ex){
        log.error("handleAnswerNotExistException",ex);
        final ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex){
        log.error("handleException",ex);
        final ErrorResponse response = new ErrorResponse(ErrorCode.INTER_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}