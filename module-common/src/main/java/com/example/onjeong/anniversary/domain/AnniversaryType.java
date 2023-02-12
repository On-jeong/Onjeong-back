package com.example.onjeong.anniversary.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AnniversaryType {
    ANNIVERSARY("ANNIVERSARY"),
    SPECIAL_SCHEDULE("SPECIAL_SCHEDULE");

    private final String value;
}
