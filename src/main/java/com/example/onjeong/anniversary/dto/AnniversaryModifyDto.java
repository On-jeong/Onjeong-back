package com.example.onjeong.anniversary.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class AnniversaryModifyDto {
    private String anniversaryContent;
    private String anniversaryType;
    private LocalDate anniversaryDate;
}
