package com.example.onjeong.anniversary.dto;

import com.example.onjeong.anniversary.domain.AnniversaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnniversaryDto {
    private Long anniversaryId;
    private String anniversaryContent;
    private AnniversaryType anniversaryType;
}
