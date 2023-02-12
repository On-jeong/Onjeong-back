package com.example.onjeong.util;

import com.example.onjeong.coin.domain.CoinHistory;
import com.example.onjeong.coin.domain.CoinHistoryType;
import com.example.onjeong.family.domain.Family;
import com.example.onjeong.user.domain.User;

import java.time.LocalDateTime;

public class CoinHistoryUtils {

    public static CoinHistory getRandomCoinHistory(User user, CoinHistoryType coinHistoryType, int coinAmount){
        final LocalDateTime coinHistoryDate = LocalDateTime.now();
        final int coinFlower = 1;
        final Family family = user.getFamily();
        return getCoinHistory(coinHistoryType, coinAmount, coinHistoryDate, coinFlower, family, user);
    }

    private static CoinHistory getCoinHistory(CoinHistoryType coinHistoryType, int coinAmount, LocalDateTime coinHistoryDate, int coinFlower, Family family, User user){
        return CoinHistory.builder()
                .coinHistoryType(coinHistoryType)
                .coinAmount(coinAmount)
                .coinHistoryDate(coinHistoryDate)
                .coinFlower(coinFlower)
                .family(family)
                .user(user)
                .build();
    }
}