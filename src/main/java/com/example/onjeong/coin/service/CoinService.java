package com.example.onjeong.coin.service;

import com.example.onjeong.coin.domain.CoinHistory;
import com.example.onjeong.coin.domain.CoinHistoryType;
import com.example.onjeong.family.domain.Family;
import com.example.onjeong.home.domain.*;
import com.example.onjeong.coin.dto.CoinHistoryDto;
import com.example.onjeong.coin.repository.CoinHistoryRepository;
import com.example.onjeong.home.repository.FlowerRepository;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.user.repository.UserRepository;
import com.example.onjeong.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CoinService {

    private final UserRepository userRepository;
    private final CoinHistoryRepository coinHistoryRepository;
    private final FlowerRepository flowerRepository;
    private final AuthUtil authUtil;

    @Transactional
    public List<CoinHistory> coinSave(CoinHistoryType coinHistoryType, int amount){

        List<CoinHistory> coinHistoryList = new ArrayList<>();
        User user = authUtil.getUserByAuthentication();
        Family family = user.getFamily();

        // 가족 코인 수 업데이트
        family.updateCoin(amount);

        // 코인 사용 내역 저장
        CoinHistory coinHistory = CoinHistory.builder()
                .coinAmount(amount)
                .coinHistoryType(coinHistoryType)
                .coinHistoryDate(LocalDateTime.now())
                .user(user)
                .family(family)
                .build();
        coinHistoryList.add(coinHistory);
        coinHistoryRepository.save(coinHistory);

        // 변경 된 코인 수에 따라 꽃 상태 변경
        Flower flower = flowerRepository.findBlooming(family.getFamilyId());

        int level = flower.getFlowerLevel();
        if(level < 10){
            if((family.getFamilyCoin() / 1000) != 0){
                // 레벨을 상승시켜야 할 정도로 코인이 쌓였을 경우
                family.updateCoin(-1000);
                CoinHistory coinHistory2 = CoinHistory.builder()
                        .coinAmount(-1000)
                        .coinHistoryType(CoinHistoryType.USED)
                        .coinHistoryDate(LocalDateTime.now())
                        .coinFlower(flower.getFlowerLevel()+1)
                        .user(user)
                        .family(family)
                        .build();
                coinHistoryRepository.save(coinHistory2);
                coinHistoryList.add(coinHistory2);
                flower.levelUp();
            }
        }

        if(flower.getFlowerLevel() == 10) {
            // 새로운 꽃 추가
            Flower newFlower = Flower.builder()
                    .flowerBloom(false)
                    .flowerKind(FlowerKind.values()[new Random().nextInt(FlowerKind.values().length)])
                    .flowerColor(FlowerColor.values()[new Random().nextInt(FlowerColor.values().length)])
                    .flowerLevel(1)
                    .family(family)
                    .build();

            flowerRepository.save(newFlower);
        }

        return coinHistoryList;
    }

    public List<CoinHistoryDto> coinHistoryList(Pageable pageable){

        User user = authUtil.getUserByAuthentication();
        Family family = user.getFamily();

        List<CoinHistory> coinHistoryList = coinHistoryRepository.findByFamily(pageable, family.getFamilyId()).toList();
        List<CoinHistoryDto> coinHistoryDtoList = new ArrayList<>();

        for(CoinHistory c : coinHistoryList){
            CoinHistoryDto coinHistoryDto = CoinHistoryDto.builder()
                    .amount(c.getCoinAmount())
                    .type(c.getCoinHistoryType())
                    .user(c.getUser() == null ? null : c.getUser().getUserStatus())
                    .date(c.getCoinHistoryDate() == null ? null : c.getCoinHistoryDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")))
                    .before(c.getCoinFlower() == null ? null : c.getCoinFlower()-1)
                    .after(c.getCoinFlower() == null ? null : c.getCoinFlower()-1)
                    .build();

            coinHistoryDtoList.add(coinHistoryDto);
        }

        return coinHistoryDtoList;
    }

    public int coinShow(){

        User user = authUtil.getUserByAuthentication();
        Family family = user.getFamily();

        return family.getFamilyCoin();
    }
}