package com.example.onjeong.family;

import com.example.onjeong.family.domain.Family;
import com.example.onjeong.family.repository.FamilyRepository;
import com.example.onjeong.util.FamilyUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FamilyRepositoryTest {

    @Autowired
    private FamilyRepository familyRepository;

    @Test
    void 가족_한가구_삭제(){
        //given
        final Family family= FamilyUtils.getRandomFamily();
        familyRepository.save(family);

        //when
        familyRepository.delete(family);

        //then
        assertThat(familyRepository.findById(family.getFamilyId()).isPresent()).isEqualTo(false);
    }
}
