package com.example.onjeong.profile.service;

import com.example.onjeong.family.domain.Family;
import com.example.onjeong.profile.domain.CoinHistory;
import com.example.onjeong.profile.domain.CoinHistoryType;
import com.example.onjeong.profile.domain.Flower;
import com.example.onjeong.profile.dto.CoinHistoryDto;
import com.example.onjeong.profile.repository.CoinHistoryRepository;
import com.example.onjeong.profile.repository.FlowerRepository;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        Optional<User> user = userRepository.findByUserNickname(authentication.getName());
        Family family = user.get().getFamily();

        // 가족 코인 수 업데이트
        family.updateCoin(amount);

        // 코인 사용 내역 저장
        CoinHistory coinHistory = CoinHistory.builder()
                .amount(amount)
                .type(coinHistoryType)
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
                        .amount(-2000)
                        .type(CoinHistoryType.USED)
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
                        .amount(-1000)
                        .type(CoinHistoryType.USED)
                        .family(family)
                        .build();
                coinHistoryRepository.save(coinHistory2);
                flower.levelUp();
            }
        }

        if(flower.getFlowerLevel() == 20) { // 만렙이 되면 새로운 꽃 추가
            Flower newFlower = Flower.builder()
                    .flowerBloom(false)
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
        Optional<User> sendUser = userRepository.findByUserNickname(authentication.getName());
        Family family = sendUser.get().getFamily();

        List<CoinHistory> coinHistoryList = coinHistoryRepository.findByFamily(family);
        List<CoinHistoryDto> coinHistoryDtoList = new ArrayList<>();

        for(CoinHistory c : coinHistoryList){
            CoinHistoryDto coinHistoryDto = CoinHistoryDto.builder()
                    .amount(c.getAmount())
                    .type(c.getType())
                    .build();
            coinHistoryDtoList.add(coinHistoryDto);
        }

        return coinHistoryDtoList;
    }

    public int coinShow(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> sendUser = userRepository.findByUserNickname(authentication.getName());
        Family family = sendUser.get().getFamily();

        return family.getFamilyCoin();
    }
}
