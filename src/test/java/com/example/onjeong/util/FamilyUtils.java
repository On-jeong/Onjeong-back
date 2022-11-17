package com.example.onjeong.util;

import com.example.onjeong.family.domain.Family;
import com.example.onjeong.profile.domain.Profile;
import com.example.onjeong.user.domain.User;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class FamilyUtils {

    public static Family getRandomFamily(){
        final Long familyId= 50L;
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
