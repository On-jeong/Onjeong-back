package com.example.onjeong.util;

import com.example.onjeong.family.domain.Family;

import java.util.Random;

public class FamilyUtils {

    public static Family getRandomFamily(){
        final Long familyId= new Random().nextLong();
        final Integer familyCoin= new Random().nextInt(1000);
        return getFamily(familyId, familyCoin);
    }

    public static Family getFamily(Long familyId, Integer familyCoin){
        return Family.builder()
                .familyId(familyId)
                .familyCoin(familyCoin)
                .build();
    }
}