package com.example.onjeong.util;

import com.example.onjeong.profile.domain.Expression;
import com.example.onjeong.profile.domain.Favorite;
import com.example.onjeong.profile.domain.Profile;
import org.apache.commons.lang3.RandomStringUtils;

public class FavoriteUtils {

    public static Favorite getRandomFavorite(Profile profile){
        final Long favoriteId= 50L;
        final String favoriteContent= RandomStringUtils.random(8, true, true);
        return getFavorite(favoriteId, favoriteContent, profile);
    }


    public static Favorite getFavorite(Long favoriteId, String favoriteContent, Profile profile){
        return Favorite.builder()
                .favoriteId(favoriteId)
                .favoriteContent(favoriteContent)
                .profile(profile)
                .build();
    }
}
