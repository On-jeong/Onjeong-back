package com.example.onjeong.home;

import com.example.onjeong.family.domain.Family;
import com.example.onjeong.coin.domain.CoinHistory;
import com.example.onjeong.coin.repository.CoinHistoryRepository;
import com.example.onjeong.util.FamilyUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CoinRepositoryTest {

    @Autowired
    CoinHistoryRepository coinHistoryRepository;

    @Test
    void 코인적립내역확인(){
        //given
        final Family family = FamilyUtils.getFamily(50L, 540);

        //when
        List<CoinHistory> coinHistoryList = coinHistoryRepository.findByFamily(family.getFamilyId());

        //then
        for(int i=0; i<coinHistoryList.size()-1; i++){
            assertTrue(coinHistoryList.get(i).getCoinHistoryDate().isAfter(coinHistoryList.get(i+1).getCoinHistoryDate()));
        }
    }
}