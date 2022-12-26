package com.example.onjeong.util;

import com.example.onjeong.profile.domain.Expression;
import com.example.onjeong.profile.domain.Hate;
import com.example.onjeong.profile.domain.Profile;
import org.apache.commons.lang3.RandomStringUtils;

public class HateUtils {

    public static Hate getRandomHate(Profile profile){
        final Long hateId= 50L;
        final String hateContent= RandomStringUtils.random(8, true, true);
        return getHate(hateId, hateContent, profile);
    }


    public static Hate getHate(Long hateId, String hateContent, Profile profile){
        return Hate.builder()
                .hateId(hateId)
                .hateContent(hateContent)
                .profile(profile)
                .build();
    }
}