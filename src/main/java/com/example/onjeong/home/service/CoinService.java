package com.example.onjeong.home.service;

import com.example.onjeong.error.ErrorCode;
import com.example.onjeong.family.domain.Family;
import com.example.onjeong.home.domain.*;
import com.example.onjeong.home.dto.CoinHistoryDto;
import com.example.onjeong.home.repository.CoinHistoryRepository;
import com.example.onjeong.home.repository.FlowerRepository;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.user.exception.UserNotExistException;
import com.example.onjeong.user.repository.UserRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CoinService {

    private final UserRepository userRepository;
    private final CoinHistoryRepository coinHistoryRepository;
    private final FlowerRepository flowerRepository;

    @Transactional
    public CoinHistoryDto coinSave(CoinHistoryType coinHistoryType, int amount){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUserNickname(authentication.getName())
                .orElseThrow(()-> new UserNotExistException("login user not exist", ErrorCode.USER_NOTEXIST));
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
        coinHistoryRepository.save(coinHistory);

        // 변경 된 코인 수에 따라 꽃 상태 변경
        Flower flower = flowerRepository.findBlooming(family.getFamilyId());

        int level = flower.getFlowerLevel();
        if(level >= 10 && level < 20){
            if((family.getFamilyCoin() / 2000) != 0){
                // 레벨을 상승시켜야 할 정도로 코인이 쌓였을 경우
                family.updateCoin(-2000);
                CoinHistory coinHistory2 = CoinHistory.builder()
                        .coinAmount(-2000)
                        .coinHistoryType(CoinHistoryType.USED)
                        .coinHistoryDate(LocalDateTime.now())
                        .coinFlower(flower.getFlowerLevel()+1)
                        .user(user)
                        .family(family)
                        .build();
                coinHistoryRepository.save(coinHistory2);
                flower.levelUp();
            }
        }
        else if(level < 10){
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
                flower.levelUp();
            }
        }

        if(flower.getFlowerLevel() == 20) {
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

        CoinHistoryDto coinHistoryDto = CoinHistoryDto.builder()
                .type(coinHistoryType)
                .amount(amount)
                .build();

        return coinHistoryDto;
    }

    public List<CoinHistoryDto> coinHistoryList(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUserNickname(authentication.getName())
                .orElseThrow(()-> new UserNotExistException("login user not exist", ErrorCode.USER_NOTEXIST));
        Family family = user.getFamily();

        List<CoinHistory> coinHistoryList = coinHistoryRepository.findByFamily(family.getFamilyId());
        List<CoinHistoryDto> coinHistoryDtoList = new ArrayList<>();

        for(CoinHistory c : coinHistoryList){
            CoinHistoryDto.CoinHistoryDtoBuilder builder = CoinHistoryDto.builder()
                    .amount(c.getCoinAmount())
                    .type(c.getCoinHistoryType());

            if (c.getUser() != null) builder.user(c.getUser().getUserStatus());
            if (c.getCoinHistoryDate() != null) builder.date(c.getCoinHistoryDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
            if (c.getCoinFlower() != null) {
                builder.before(c.getCoinFlower()-1);
                builder.after(c.getCoinFlower());
            }

            CoinHistoryDto coinHistoryDto = builder.build();
            coinHistoryDtoList.add(coinHistoryDto);
        }

        return coinHistoryDtoList;
    }

    public int coinShow(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUserNickname(authentication.getName())
                .orElseThrow(()-> new UserNotExistException("login user not exist", ErrorCode.USER_NOTEXIST));
        Family family = user.getFamily();

        return family.getFamilyCoin();
    }
}
