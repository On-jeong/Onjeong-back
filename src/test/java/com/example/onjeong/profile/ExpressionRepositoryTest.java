package com.example.onjeong.profile;

import com.example.onjeong.family.domain.Family;
import com.example.onjeong.profile.domain.Expression;
import com.example.onjeong.profile.domain.Profile;
import com.example.onjeong.profile.repository.ExpressionRepository;
import com.example.onjeong.profile.repository.ProfileRepository;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.util.ExpressionUtils;
import com.example.onjeong.util.FamilyUtils;
import com.example.onjeong.util.ProfileUtils;
import com.example.onjeong.util.UserUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ExpressionRepositoryTest {

    @Autowired
    private ExpressionRepository expressionRepository;


    @Test
    void Expression여러개_삭제(){
        //given
        final Profile profile= profile();
        final List<Expression> expressions= new ArrayList<>();
        for(int i=0;i<3;i++){
            final Expression expression= ExpressionUtils.getRandomExpression(profile);
            expressions.add(expression);
        }
        expressionRepository.saveAll(expressions);


        //when
        expressionRepository.deleteAllByProfile(profile);


        //then
    }


    private Profile profile(){
        final Family family= FamilyUtils.getRandomFamily();
        final User user= UserUtils.getRandomUser(family);
        return ProfileUtils.getRandomProfile(family, user);
    }
}
