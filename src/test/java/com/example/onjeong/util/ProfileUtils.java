package com.example.onjeong.util;

import com.example.onjeong.family.domain.Family;
import com.example.onjeong.profile.domain.Profile;
import com.example.onjeong.user.domain.User;
import org.apache.commons.lang3.RandomStringUtils;


public class ProfileUtils {

    public static Profile getRandomProfile(Family family, User user){
        final Long profileId= 50L;
        final String message= RandomStringUtils.random(8, true, true);
        final String profileImageUrl= null;
        final boolean checkProfileImage= true;
        final boolean checkProfileUpload= true;
        return getProfile(profileId, message, null, checkProfileImage, checkProfileUpload, family, user);
    }


    public static Profile getProfile(Long profileId, String message, String profileImageUrl, boolean checkProfileImage,
                                     boolean checkProfileUpload, Family family, User user){
        return Profile.builder()
                .profileId(profileId)
                .message(message)
                .profileImageUrl(profileImageUrl)
                .checkProfileImage(checkProfileImage)
                .checkProfileUpload(checkProfileUpload)
                .family(family)
                .user(user)
                .build();
    }
}
