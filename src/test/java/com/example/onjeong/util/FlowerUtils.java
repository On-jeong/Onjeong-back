package com.example.onjeong.util;

import com.example.onjeong.anniversary.domain.Anniversary;
import com.example.onjeong.anniversary.domain.AnniversaryType;
import com.example.onjeong.family.domain.Family;
import com.example.onjeong.home.domain.Flower;
import com.example.onjeong.home.domain.FlowerColor;
import com.example.onjeong.home.domain.FlowerKind;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;
import java.util.Random;

public class FlowerUtils {

    public static Flower getRandomFlower(Family family){
        final Long flowerId= 50L;
        final FlowerKind flowerKind= FlowerKind.ROSE;
        final FlowerColor flowerColor= FlowerColor.RED;
        final Boolean flowerBloom= true;
        final Integer flowerLevel= new Random().nextInt(10);
        final LocalDate flowerBloomDate= LocalDate.now();
        return getFlower(flowerId, flowerKind, flowerColor, flowerBloom, flowerLevel, flowerBloomDate, family);
    }


    public static Flower getFlower(Long flowerId, FlowerKind flowerKind, FlowerColor flowerColor, Boolean flowerBloom,
                                   Integer flowerLevel, LocalDate flowerBloomDate, Family family){
        return Flower.builder()
                .flowerId(flowerId)
                .flowerKind(flowerKind)
                .flowerColor(flowerColor)
                .flowerBloom(flowerBloom)
                .flowerLevel(flowerLevel)
                .flowerBloomDate(flowerBloomDate)
                .family(family)
                .build();
    }
}
