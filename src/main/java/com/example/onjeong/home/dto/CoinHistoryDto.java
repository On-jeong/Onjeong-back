package com.example.onjeong.home.dto;

import com.example.onjeong.home.domain.CoinHistoryType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoinHistoryDto {

    private CoinHistoryType type;
    private Integer amount;
    private String date;
    private String user;

    private Integer before;
    private Integer after;

}
