package com.example.onjeong.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    NOT_FOUND(404,"COMMON-ERR-404","PAGE NOT FOUND"),
    INTER_SERVER_ERROR(500,"COMMON-ERR-500","INTER SERVER ERROR"),
    EMAIL_DUPLICATION(400,"MEMBER-ERR-400","EMAIL DUPLICATED"),

    USER_NOTEXIST(500,"U001","LOGIN USER NOT EXIST"),

    RECEIVEUSER_NOTEXIST(500,"M001","RECEIVING USER NOT EXIST"),
    MAIL_NOTEXIST(500,"M002","MAIL NOT EXIST"),

    QUESTION_NOTEXIST(500,"Q001","WEEKLY QUESTION NOT EXIST"),
    ANSWER_NOTEXIST(500,"Q002","ANSWER NOT EXIST"),
    ;

    final private int status;
    final private String errorCode;
    final private String message;
}