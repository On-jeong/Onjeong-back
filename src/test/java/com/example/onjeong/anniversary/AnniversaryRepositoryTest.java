package com.example.onjeong.anniversary;

import com.example.onjeong.anniversary.domain.Anniversary;
import com.example.onjeong.anniversary.repository.AnniversaryRepository;
import com.example.onjeong.family.domain.Family;
import com.example.onjeong.util.AnniversaryUtils;
import com.example.onjeong.util.FamilyUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AnniversaryRepositoryTest {

    @Autowired
    private AnniversaryRepository anniversaryRepository;

    @Test
    void 기념일_여러개_삭제(){
        //given
        final Family family= FamilyUtils.getRandomFamily();
        final List<Anniversary> anniversaries= new ArrayList<>();

        for(int i=0;i<3;i++){
            final Anniversary anniversary= AnniversaryUtils.getRandomAnniversary(family);
            anniversaries.add(anniversary);
        }
        anniversaryRepository.saveAll(anniversaries);


        //when
        anniversaryRepository.deleteAllByFamily(family);


        //then
        assertThat(anniversaryRepository.findAllByFamily(family).size()).isEqualTo(0);
    }
}
