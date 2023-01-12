package com.example.onjeong.home;

import com.example.onjeong.family.domain.Family;
import com.example.onjeong.coin.domain.CoinHistory;
import com.example.onjeong.coin.repository.CoinHistoryRepository;
import com.example.onjeong.util.FamilyUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
        final Pageable pageable = PageRequest.of(0, 20);
        //when
        Page<CoinHistory> coinHistoryPage = coinHistoryRepository.findByFamily(pageable, family.getFamilyId());

        //then
        List<CoinHistory> coinHistoryList = coinHistoryPage.toList();
        for(int i=0; i<coinHistoryList.size()-1; i++){
            assertTrue(coinHistoryList.get(i).getCoinHistoryDate().isAfter(coinHistoryList.get(i+1).getCoinHistoryDate()));
        }
    }
}