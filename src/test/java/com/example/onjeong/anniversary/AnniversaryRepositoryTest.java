package com.example.onjeong.anniversary;

import com.example.onjeong.anniversary.domain.Anniversary;
import com.example.onjeong.anniversary.repository.AnniversaryRepository;
import com.example.onjeong.family.domain.Family;
import com.example.onjeong.family.repository.FamilyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AnniversaryRepositoryTest {

    @Autowired
    private AnniversaryRepository anniversaryRepository;

    @Autowired
    private FamilyRepository familyRepository;

    private final Long familyId= 1L;

    private final LocalDate anniversaryDate= LocalDate.of(2022,12,03);


    @Test
    void 월별_모든기념일_조회_최대3개(){
        //given


        //when
        List<Anniversary> anniversaries= anniversaryRepository.findByAnniversaryDate(anniversaryDate, familyId);


        //then
        assertThat(anniversaries.size()).isEqualTo(3);
    }


    @Test
    void 일별_모든기념일_조회(){
        //given
        Family family= familyRepository.findById(familyId).get();


        //when
        List<Anniversary> anniversaries= anniversaryRepository.findAllByAnniversaryDateAndFamily(anniversaryDate, family);


        //then
        assertThat(anniversaries.size()).isEqualTo(5);
    }


    @Test
    void 해당기념일_삭제(){
        //given
        final Long anniversaryId= 1L;
        Family family= familyRepository.findById(familyId).get();


        //when
        anniversaryRepository.deleteByAnniversaryIdAndFamily(anniversaryId, family);


        //then

    }


    @Test
    void 기념일_여러개_삭제(){
        //given
        Family family= familyRepository.findById(familyId).get();


        //when
        anniversaryRepository.deleteAllByFamily(family);


        //then

    }
}
