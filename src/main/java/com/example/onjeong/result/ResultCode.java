package com.example.onjeong.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {
    // User
    REGISTER_SUCCESS(200, "U001", "회원가입에 성공했습니다."),
    LOGIN_SUCCESS(200, "U002", "로그인에 성공했습니다."),
    LOGOUT_SUCCESS(200,"U003","로그아웃에 성공했습니다."),
    MODIFY_USER_INFORMATION_SUCCESS(200,"U004","회원정보를 수정했습니다."),
    DELETE_USER_SUCCESS(200,"U005","회원탈퇴에 성공했습니다."),
    GET_USER_SUCCESS(200,"U006","유저 기본정보를 조회했습니다."),
    NEW_TOKEN_SUCCESS(200,"U007","새로운 Access Token을 발급했습니다."),
    GET_HOME_SUCCESS(200,"U008","home을 조회했습니다."),

    // Anniversary
    GET_ALL_ANNIVERSARY_SUCCESS(200, "A001", "월별 모든 특수일정을 조회했습니다."),
    GET_ANNIVERSARY_SUCCESS(200, "A002", "해당 일의 특수일정을 조회했습니다."),
    REGISTER_ANNIVERSARY_SUCCESS(200, "A003", "해당 일의 특수일정을 등록했습니다."),
    DELETE_ANNIVERSARY_SUCCESS(200, "A004", "해당 일의 특수일정을 삭제했습니다."),

    // Board
    GET_ALL_BOARD_SUCCESS(200, "B001", "모든 오늘의 기록을 조회했습니다."),
    GET_BOARD_SUCCESS(200, "B002", "오늘의 기록을 조회했습니다."),
    REGISTER_BOARD_SUCCESS(200, "B003", "오늘의 기록을 등록했습니다."),
    MODIFY_BOARD_SUCCESS(200, "B004", "오늘의 기록을 수정했습니다."),
    DELETE_BOARD_SUCCESS(200, "B005", "오늘의 기록을 삭제했습니다."),

    // Profile
    GET_ALL_USER_SUCCESS(200,"P001","모든 가족 구성원을 조회했습니다."),
    GET_USER_INFORMATION_SUCCESS(200,"P002","프로필 상단에 보여줄 개인정보를 조회했습니다."),
    REGISTER_PROFILE_IMAGE_SUCCESS(200,"P003","프로필 사진을 등록했습니다."),
    DELETE_PROFILE_IMAGE_SUCCESS(200,"P004","프로필 사진을 삭제했습니다."),
    GET_PROFILE_MESSAGE_SUCCESS(200,"P005","상태메시지를 조회했습니다."),
    REGISTER_PROFILE_MESSAGE_SUCCESS(200,"P006","상태메시지를 등록했습니다."),
    MODIFY_PROFILE_MESSAGE_SUCCESS(200,"P007","상태메시지를 수정했습니다."),
    GET_FAVORITES_SUCCESS(200,"P008","좋아하는 것 목록을 조회했습니다."),
    REGISTER_FAVORITE_SUCCESS(200,"P009","좋아하는 것을 등록했습니다."),
    DELETE_FAVORITE_SUCCESS(200,"P010","좋아하는 것을 삭제했습니다."),
    GET_HATES_SUCCESS(200,"P011","싫어하는 것 목록을 조회했습니다."),
    REGISTER_HATE_SUCCESS(200,"P012","싫어하는 것을 등록했습니다."),
    DELETE_HATE_SUCCESS(200,"P013","싫어하는 것을 삭제했습니다."),
    GET_EXPRESSIONS_SUCCESS(200,"P014","한단어로 표현하는 것 목록을 조회했습니다."),
    REGISTER_EXPRESSION_SUCCESS(200,"P015","한단어로 표현하는 것을 등록했습니다."),
    DELETE_EXPRESSION_SUCCESS(200,"P016","한단어로 표현하는 것을 삭제했습니다."),
    GET_INTERESTS_SUCCESS(200,"P017","관심사 목록을 조회했습니다."),
    REGISTER_INTEREST_SUCCESS(200,"P018","관심사를 등록했습니다."),
    DELETE_INTEREST_SUCCESS(200,"P019","관심사를 삭제했습니다."),
    GET_PROFILE_SUCCESS(200,"P020","유저 프로필(개인정보+상태메시지)을 조회했습니다."),
    GET_INFORMATIONS_SUCCESS(200,"P021","유저 개인정보(좋아하는것, 싫어하는것..등)를 조회했습니다.")
    ;


    private final int status;
    private final String code;
    private final String message;
}
