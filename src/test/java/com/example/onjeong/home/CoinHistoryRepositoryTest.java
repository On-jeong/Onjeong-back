package com.example.onjeong.home;

import com.example.onjeong.family.domain.Family;
import com.example.onjeong.home.domain.CoinHistory;
import com.example.onjeong.home.repository.CoinHistoryRepository;
import com.example.onjeong.question.domain.Question;
import com.example.onjeong.util.CoinHistoryUtils;
import com.example.onjeong.util.FamilyUtils;
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
public class CoinHistoryRepositoryTest {

    @Autowired
    private CoinHistoryRepository coinHistoryRepository;

    @Test
    void 코인내역_여러개_삭제(){
        //given
        final Family family= FamilyUtils.getRandomFamily();
        final List<CoinHistory> coinHistories= new ArrayList<>();

        for(int i=0;i<3;i++){
            final CoinHistory coinHistory= CoinHistoryUtils.getRandomCoinHistory(family);
            coinHistories.add(coinHistory);
        }
        coinHistoryRepository.saveAll(coinHistories);


        //when
        coinHistoryRepository.deleteAllByFamily(family);


        //then
    }
}
