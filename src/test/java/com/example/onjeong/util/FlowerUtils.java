package com.example.onjeong.util;

import com.example.onjeong.family.domain.Family;
import com.example.onjeong.home.domain.*;
import com.example.onjeong.user.domain.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class FlowerUtils {

    public static Flower getRandomFlower(Family family, Integer flowerLevel, boolean flowerBloom){
        final FlowerKind flowerKind = FlowerKind.ROSE;
        final FlowerColor flowerColor = FlowerColor.GREEN;

        return getFlower(flowerKind, flowerColor, flowerBloom, flowerLevel, family);
    }

    private static Flower getFlower(FlowerKind flowerkind, FlowerColor flowerColor, boolean flowerBloom, int flowerLevel, Family family){
        return Flower.builder()
                .flowerKind(flowerkind)
                .flowerColor(flowerColor)
                .flowerBloom(flowerBloom)
                .flowerBloomDate(flowerBloom == true ? LocalDate.now() : null)
                .flowerLevel(flowerLevel)
                .family(family)
                .build();
    }
}