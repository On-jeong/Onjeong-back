package com.example.onjeong.profile;

import com.example.onjeong.family.domain.Family;
import com.example.onjeong.profile.domain.Favorite;
import com.example.onjeong.profile.domain.Profile;
import com.example.onjeong.profile.repository.FavoriteRepository;
import com.example.onjeong.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FavoriteRepositoryTest {

    @Autowired
    private FavoriteRepository favoriteRepository;


    @Test
    void deleteByFavoriteIdAndProfile테스트(){
        //given
        final Long favoriteId= 1L;
        final Profile profile= favoriteRepository.findById(favoriteId).get().getProfile();


        //when
        favoriteRepository.deleteByFavoriteIdAndProfile(favoriteId, profile);


        //then

    }


    @Test
    void Favorite여러개_삭제(){
        //given
        final Profile profile= profile();
        final List<Favorite> favorites= new ArrayList<>();
        for(int i=0;i<3;i++){
            final Favorite favorite= FavoriteUtils.getRandomFavorite(profile);
            favorites.add(favorite);
        }
        favoriteRepository.saveAll(favorites);


        //when
        favoriteRepository.deleteAllByProfile(profile);


        //then
    }


    private Profile profile(){
        final Family family= FamilyUtils.getRandomFamily();
        final User user= UserUtils.getRandomUser(family);
        return ProfileUtils.getRandomProfile(family, user);
    }
}