package com.example.onjeong.profile;

import com.example.onjeong.family.domain.Family;
import com.example.onjeong.profile.domain.Expression;
import com.example.onjeong.profile.domain.Hate;
import com.example.onjeong.profile.domain.Profile;
import com.example.onjeong.profile.repository.ExpressionRepository;
import com.example.onjeong.profile.repository.HateRepository;
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
public class HateRepositoryTest {

    @Autowired
    private HateRepository hateRepository;


    @Test
    void Hate여러개_삭제(){
        //given
        final Profile profile= profile();
        final List<Hate> hates= new ArrayList<>();
        for(int i=0;i<3;i++){
            final Hate hate= HateUtils.getRandomHate(profile);
            hates.add(hate);
        }
        hateRepository.saveAll(hates);


        //when
        hateRepository.deleteAllByProfile(profile);


        //then
    }


    private Profile profile(){
        final Family family= FamilyUtils.getRandomFamily();
        final User user= UserUtils.getRandomUser(family);
        return ProfileUtils.getRandomProfile(family, user);
    }
}
