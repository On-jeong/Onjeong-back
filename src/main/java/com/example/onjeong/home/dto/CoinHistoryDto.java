package com.example.onjeong.home.dto;

import com.example.onjeong.home.domain.CoinHistoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CoinHistoryDto {

    private CoinHistoryType type;
    private Integer amount;
    private boolean bloomFlag;

}
