package com.example.onjeong.home;

import com.example.onjeong.family.domain.Family;
import com.example.onjeong.home.domain.CoinHistory;
import com.example.onjeong.home.domain.CoinHistoryType;
import com.example.onjeong.home.domain.Flower;
import com.example.onjeong.home.dto.CoinHistoryDto;
import com.example.onjeong.home.repository.CoinHistoryRepository;
import com.example.onjeong.home.repository.FlowerRepository;
import com.example.onjeong.home.service.CoinService;
import com.example.onjeong.mail.domain.Mail;
import com.example.onjeong.mail.dto.MailDto;
import com.example.onjeong.mail.dto.MailRequestDto;
import com.example.onjeong.mail.exception.MailNotExistException;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.util.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.format.DateTimeFormatter;
import java.util.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoinServiceTest {

    @InjectMocks
    private CoinService coinService;

    @Mock
    private CoinHistoryRepository coinHistoryRepository;

    @Mock
    private FlowerRepository flowerRepository;

    @Mock
    private AuthUtil authUtil;

    @Nested
    class 코인적립{
        @Test
        @DisplayName("레벨 업 되지 않고 코인 적립만 된 경우")
        void 일반적립(){
            //given
            final Family family = FamilyUtils.getRandomFamily();
            final User user = UserUtils.getRandomUser(family);
            final Flower flower = FlowerUtils.getRandomFlower(family, 1, false);

            final CoinHistoryType coinHistoryType = CoinHistoryType.RAND;
            final int coinAmount = 100;
            final int initialCoinAmount = family.getFamilyCoin();

            doReturn(user).when(authUtil).getUserByAuthentication();
            doReturn(flower).when(flowerRepository).findBlooming(family.getFamilyId());

            //when
            coinService.coinSave(coinHistoryType, coinAmount);

            //then
            verify(coinHistoryRepository,times(1)).save(any(CoinHistory.class));
            verify(flowerRepository,times(1)).findBlooming(family.getFamilyId());
            assertEquals(family.getFamilyCoin(), initialCoinAmount + coinAmount);
        }

        @Test
        @DisplayName("10레벨 미만에서 적립을 통해 레벨 업 되었을 경우")
        void 적립후_10레벨미만레벨업(){
            final Family family = FamilyUtils.getFamily(1L, 950);
            final User user = UserUtils.getRandomUser(family);
            final Flower flower = FlowerUtils.getRandomFlower(family, 5, false);

            final CoinHistoryType coinHistoryType = CoinHistoryType.RAND;
            final int coinAmount = 100;
            final int initialCoinAmount = family.getFamilyCoin();

            doReturn(user).when(authUtil).getUserByAuthentication();
            doReturn(flower).when(flowerRepository).findBlooming(family.getFamilyId());

            //when
            List<CoinHistory> result = coinService.coinSave(coinHistoryType, coinAmount);

            verify(coinHistoryRepository,times(2)).save(any(CoinHistory.class));
            assertEquals(family.getFamilyCoin(), initialCoinAmount + coinAmount - 1000);
            assertEquals(6, result.get(1).getCoinFlower());
        }

        @Test
        @DisplayName("10~20레벨 사이에서 적립을 통해 레벨 업 되었을 경우")
        void 적립후_10레벨이상레벨업(){
            final Family family = FamilyUtils.getFamily(1L, 1950);
            final User user = UserUtils.getRandomUser(family);
            final Flower flower = FlowerUtils.getRandomFlower(family, 15, false);

            final CoinHistoryType coinHistoryType = CoinHistoryType.RAND;
            final int coinAmount = 100;
            final int initialCoinAmount = family.getFamilyCoin();

            doReturn(user).when(authUtil).getUserByAuthentication();
            doReturn(flower).when(flowerRepository).findBlooming(family.getFamilyId());

            //when
            List<CoinHistory> result = coinService.coinSave(coinHistoryType, coinAmount);

            verify(coinHistoryRepository,times(2)).save(any(CoinHistory.class));
            assertEquals(family.getFamilyCoin(), initialCoinAmount + coinAmount - 2000);
            assertEquals(16, result.get(1).getCoinFlower());
        }

        @Test
        @DisplayName("레벨 업이 돼서 20레벨을 초과하는 경우")
        void 적립후_새로운꽃생성(){
            final Family family = FamilyUtils.getFamily(1L, 1950);
            final User user = UserUtils.getRandomUser(family);
            final Flower flower = FlowerUtils.getRandomFlower(family, 19, false);

            final CoinHistoryType coinHistoryType = CoinHistoryType.RAND;
            final int coinAmount = 100;

            doReturn(user).when(authUtil).getUserByAuthentication();
            doReturn(flower).when(flowerRepository).findBlooming(family.getFamilyId());

            //when
            coinService.coinSave(coinHistoryType, coinAmount);

            //then
            verify(coinHistoryRepository,times(2)).save(any(CoinHistory.class));
            verify(flowerRepository,times(1)).save(any(Flower.class));
            assertEquals(20, flower.getFlowerLevel());
            assertNotNull(flower.getFlowerBloomDate());
            assertTrue(flower.getFlowerBloom());
        }

    }

    @Nested
    class 코인적립내역{

        @Test
        void 꽃레벨_변동없을때(){
            final Family family = FamilyUtils.getRandomFamily();
            final User user = UserUtils.getRandomUser(family);

            List<CoinHistory> coinHistoryList = new ArrayList<>();
            coinHistoryList.add(CoinHistoryUtils.getRandomCoinHistory(user, CoinHistoryType.RAND, 10));
            coinHistoryList.add(CoinHistoryUtils.getRandomCoinHistory(user, CoinHistoryType.PROFILEIMAGE, 100));

            doReturn(user).when(authUtil).getUserByAuthentication();
            doReturn(coinHistoryList).when(coinHistoryRepository).findByFamily(family.getFamilyId());

            //when
            List<CoinHistoryDto> result = coinService.coinHistoryList();

            //then
            assertEquals(result.size(), 2);
            for(int i=0; i<2; i++){
                assertEquals(result.get(i).getType(), coinHistoryList.get(i).getCoinHistoryType());
                assertEquals(result.get(i).getAmount(), coinHistoryList.get(i).getCoinAmount());
                assertEquals(result.get(i).getDate(), coinHistoryList.get(i).getCoinHistoryDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
                assertEquals(result.get(i).getUser(), coinHistoryList.get(i).getUser().getUserStatus());
            }
        }

        @Test
        void 꽃레벨_상승할때(){
            //given
            final Family family = FamilyUtils.getRandomFamily();
            final User user = UserUtils.getRandomUser(family);

            List<CoinHistory> coinHistoryList = new ArrayList<>();
            for(int i=0; i<11; i++){
                coinHistoryList.add(CoinHistoryUtils.getRandomCoinHistory(user, CoinHistoryType.RAND, 95));
            }
            doReturn(user).when(authUtil).getUserByAuthentication();
            doReturn(coinHistoryList).when(coinHistoryRepository).findByFamily(family.getFamilyId());

            //when
            List<CoinHistoryDto> result = coinService.coinHistoryList();

            //then
            assertNotNull(result.get(10).getAfter());
            assertNotNull(result.get(10).getBefore());
        }
    }

    @Test
    void 코인갯수확인() {
        //given
        final Family family = FamilyUtils.getRandomFamily();
        final User user = UserUtils.getRandomUser(family);
        doReturn(user).when(authUtil).getUserByAuthentication();

        //when
        int coin = coinService.coinShow();

        //then
        assertEquals(family.getFamilyCoin(), coin);
    }



}