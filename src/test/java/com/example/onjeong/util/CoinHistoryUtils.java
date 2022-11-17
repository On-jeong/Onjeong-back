package com.example.onjeong.util;

import com.example.onjeong.anniversary.domain.Anniversary;
import com.example.onjeong.anniversary.domain.AnniversaryType;
import com.example.onjeong.family.domain.Family;
import com.example.onjeong.home.domain.CoinHistory;
import com.example.onjeong.home.domain.CoinHistoryType;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.MethodOrderer;

import java.time.LocalDate;
import java.util.Random;

public class CoinHistoryUtils {

    public static CoinHistory getRandomCoinHistory(Family family){
        final Long coinHistory_id= 50L;
        final CoinHistoryType type= CoinHistoryType.RAND;
        final Integer amount= new Random().nextInt(1000);
        return getCoinHistory(coinHistory_id, type, amount, family);
    }


    public static CoinHistory getCoinHistory(Long coinHistory_id, CoinHistoryType type, Integer amount, Family family){
        return CoinHistory.builder()
                .coinHistory_id(coinHistory_id)
                .type(type)
                .amount(amount)
                .family(family)
                .build();
    }
}
