package com.example.onjeong.profile;

import com.example.onjeong.family.domain.Family;
import com.example.onjeong.profile.domain.Profile;
import com.example.onjeong.profile.repository.ProfileRepository;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.util.FamilyUtils;
import com.example.onjeong.util.ProfileUtils;
import com.example.onjeong.util.UserUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProfileRepositoryTest {

    @Autowired
    private ProfileRepository profileRepository;

    @Test
    void 프로필한개_삭제(){
        //given
        final Profile profile= profile();
        profileRepository.save(profile);


        //when
        profileRepository.deleteByUser(profile.getUser());


        //then
    }


    private Profile profile(){
        final Family family= FamilyUtils.getRandomFamily();
        final User user= UserUtils.getRandomUser(family);
        return ProfileUtils.getRandomProfile(family, user);
    }
}