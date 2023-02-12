package com.example.onjeong.home;

import com.example.onjeong.family.domain.Family;
import com.example.onjeong.home.domain.Flower;
import com.example.onjeong.home.repository.FlowerRepository;
import com.example.onjeong.util.FamilyUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FlowerRepositoryTest {

    @Autowired
    FlowerRepository flowerRepository;

    @Nested
    class 꽃상태확인{

        @Test
        void 현재꽃확인() {
            //given
            final Family family = FamilyUtils.getFamily(50L, 540);

            // when
            Flower flower = flowerRepository.findBlooming(family.getFamilyId());

            // then
            assertFalse(flower.getFlowerBloom());
        }

        @Test
        void 만개한꽃확인() {
            //given
            final Family family = FamilyUtils.getFamily(50L, 540);

            // when
            List<Flower> flower = flowerRepository.findFullBloom(family.getFamilyId());

            // then
            for(Flower f : flower){
                assertTrue(f.getFlowerBloom());
            }
        }

    }
}