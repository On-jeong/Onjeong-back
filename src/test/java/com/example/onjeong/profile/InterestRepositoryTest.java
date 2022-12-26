package com.example.onjeong.profile;

import com.example.onjeong.family.domain.Family;
import com.example.onjeong.profile.domain.Hate;
import com.example.onjeong.profile.domain.Interest;
import com.example.onjeong.profile.domain.Profile;
import com.example.onjeong.profile.repository.HateRepository;
import com.example.onjeong.profile.repository.InterestRepository;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.util.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class InterestRepositoryTest {

    @Autowired
    private InterestRepository interestRepository;


    @Test
    void deleteByInterestIdAndProfile테스트(){
        //given
        final Long interestId= 1L;
        final Profile profile= interestRepository.findById(interestId).get().getProfile();


        //when
        interestRepository.deleteByInterestIdAndProfile(interestId, profile);


        //then

    }


    @Test
    void Interest여러개_삭제(){
        //given
        final Profile profile= profile();
        final List<Interest> interests= new ArrayList<>();
        for(int i=0;i<3;i++){
            final Interest interest= InterestUtils.getRandomInterest(profile);
            interests.add(interest);
        }
        interestRepository.saveAll(interests);


        //when
        interestRepository.deleteAllByProfile(profile);


        //then
    }


    private Profile profile(){
        final Family family= FamilyUtils.getRandomFamily();
        final User user= UserUtils.getRandomUser(family);
        return ProfileUtils.getRandomProfile(family, user);
    }
}