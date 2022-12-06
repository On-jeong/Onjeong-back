package com.example.onjeong.home;

import com.example.onjeong.family.domain.Family;
import com.example.onjeong.home.domain.Flower;
import com.example.onjeong.home.repository.FlowerRepository;
import com.example.onjeong.question.domain.Question;
import com.example.onjeong.util.FamilyUtils;
import com.example.onjeong.util.FlowerUtils;
import com.example.onjeong.util.QuestionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FlowerRepositoryTest {

    @Autowired
    private FlowerRepository flowerRepository;

    @Test
    void 꽃_여러개_삭제(){
        //given
        final Family family= FamilyUtils.getRandomFamily();
        final List<Flower> flowers= new ArrayList<>();

        for(int i=0;i<3;i++){
            final Flower flower= FlowerUtils.getRandomFlower(family);
            flowers.add(flower);
        }
        flowerRepository.saveAll(flowers);


        //when
        flowerRepository.deleteAllByFamily(family);


        //then
    }
}
