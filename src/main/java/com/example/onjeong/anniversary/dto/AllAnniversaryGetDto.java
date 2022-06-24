package com.example.onjeong.anniversary.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class AllAnniversaryGetDto {
    private LocalDate anniversaryDate;
}
