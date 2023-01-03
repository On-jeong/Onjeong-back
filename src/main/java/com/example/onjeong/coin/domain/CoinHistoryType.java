package com.example.onjeong.coin.domain;

public enum CoinHistoryType {
    RAND, USED, MAIL, BOARD, ANSWER,
    PROFILEIMAGE, PROFILEMESSAGE, PROFILEFAV, PROFILEHATE, PROFILEEXPRESSION, PROFILEINTEREST
}

// RAND : 랜덤 코인 지급
// USED : 포인트가 꽃 피우는데 사용됨
// MAIL : 메일 작성 시 지급된 포인트
// PROFILE : 프로필 작성 시 지급된 포인트
// BOARD : 오늘의 기록 작성 시 지급된 포인트
// ANSWER : 이달의문답 작성 시 지급된 포인트