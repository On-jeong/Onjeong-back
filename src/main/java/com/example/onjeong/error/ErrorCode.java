package com.example.onjeong.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    NOT_FOUND(404,"COMMON-ERR-404","PAGE NOT FOUND"),
    INTER_SERVER_ERROR(500,"COMMON-ERR-500","INTER SERVER ERROR"),
    EMAIL_DUPLICATION(400,"MEMBER-ERR-400","EMAIL DUPLICATED"),
    DATETYPE_ERROR(500,"DATETYPE-ERR-500","DATETYPE CONVERSE FAIL"),
    TOKEN_EXPIRED(401,"TOKEN-ERR-401","TOKEN EXPIRED ERROR"),
    FILE_UPLOAD_ERROR(500,"FILE-ERR-500","FILE UPLOAD ERROR"),
    UPLOAD_FILE_NOTEXIST(500,"FILE-ERR-500","UPLOAD FILE NOT EXIST"),

    REFRESH_TOKEN_EXPIRED(401,"A002","REFRESH TOKEN EXPIRED"),
    INPUT_NOT_FOUND(400,"A003","INPUT ERROR"),
    ACCESS_TOKEN_EXPIRED(401,"A004","ACCESS TOKEN EXPIRED"),
    ACCESS_TOKEN_NOT_SAME(401,"A005","ACCESS TOKEN NOT SAME"),

    FAMILY_NOTEXIST(500,"F001","FAMILY NOT EXIST"),

    USER_NOTEXIST(500,"U001","LOGIN USER NOT EXIST"),
    USER_UNAUTHORIZED(401,"U002","USER UNAUTHORIZED"),
    USER_NICKNAME_DUPLICATION(400,"U003","USER NICKNAME DUPLICATION"),
    JOINED_USER_NOTEXIST(500,"U004","JOINED USER NOT EXIST"),

    PROFILE_NOTEXIST(500,"P001","PROFILE NOT EXIST"),

    BOARD_NOTEXIST(500,"B001","BOARD NOT EXIST"),
    BOARD_WRITER_NOT_SAME(500,"BO02","BOARD WRITER NOT SAME"),

    RECEIVEUSER_NOTEXIST(500,"M001","RECEIVING USER NOT EXIST"),
    MAIL_NOTEXIST(500,"M002","MAIL NOT EXIST"),

    QUESTION_NOTEXIST(500,"Q001","WEEKLY QUESTION NOT EXIST"),
    ANSWER_NOTEXIST(500,"Q002","ANSWER NOT EXIST"),
    ;

    final private int status;
    final private String errorCode;
    final private String message;
}