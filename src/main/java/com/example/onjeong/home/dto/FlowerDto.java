package com.example.onjeong.home.dto;

import com.example.onjeong.home.domain.FlowerColor;
import com.example.onjeong.home.domain.FlowerKind;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FlowerDto {
    private FlowerKind flowerKind;
    private FlowerColor flowerColor;
    private int flowerLevel;
    private LocalDate flowerBloomDate;
}
