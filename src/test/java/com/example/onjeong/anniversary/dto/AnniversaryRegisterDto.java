package com.example.onjeong.anniversary.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class AnniversaryRegisterDto {
    private String anniversaryContent;
    private String anniversaryType;
}
