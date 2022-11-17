package com.example.onjeong.util;

import com.example.onjeong.profile.domain.Expression;
import com.example.onjeong.profile.domain.Interest;
import com.example.onjeong.profile.domain.Profile;
import org.apache.commons.lang3.RandomStringUtils;

public class InterestUtils {

    public static Interest getRandomInterest(Profile profile){
        final Long interestId= 50L;
        final String interestContent= RandomStringUtils.random(8, true, true);
        return getInterest(interestId, interestContent, profile);
    }


    public static Interest getInterest(Long interestId, String interestContent, Profile profile){
        return Interest.builder()
                .interestId(interestId)
                .interestContent(interestContent)
                .profile(profile)
                .build();
    }
}
