package com.example.onjeong.home.service;

import com.example.onjeong.coin.domain.CoinHistory;
import com.example.onjeong.coin.domain.CoinHistoryType;
import com.example.onjeong.coin.repository.CoinHistoryRepository;
import com.example.onjeong.error.ErrorCode;
import com.example.onjeong.family.domain.Family;
import com.example.onjeong.home.domain.Flower;
import com.example.onjeong.home.domain.FlowerKind;
import com.example.onjeong.home.dto.FlowerDto;
import com.example.onjeong.home.repository.FlowerRepository;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.user.exception.UserNotExistException;
import com.example.onjeong.user.repository.UserRepository;
import com.example.onjeong.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class HomeService {

    private final FlowerRepository flowerRepository;
    private final CoinHistoryRepository coinHistoryRepository;
    private final AuthUtil authUtil;

    public Pair<Boolean, Integer> randomCoin(){
        User user = authUtil.getUserByAuthentication();

        int randAmount = (int) (Math.random() * (100 - 10 + 1)) + 10; // 10~100 사이 랜덤한 수
        List<CoinHistory> coinHistoryList = coinHistoryRepository.findByDateAndType(user.getUserId());

        if(coinHistoryList.size() == 0) return Pair.of(true, randAmount);
        else return Pair.of(false, randAmount);
    }

    public FlowerDto showFlower(){

        User user = authUtil.getUserByAuthentication();
        Family family = user.getFamily();

        Flower flower = flowerRepository.findBlooming(family.getFamilyId());
        FlowerDto flowerDto = FlowerDto.builder()
                .flowerLevel(flower.getFlowerLevel())
                .flowerKind(flower.getFlowerKind())
                .flowerColor(flower.getFlowerColor())
                .build();
        return flowerDto;

    }

    public List<FlowerDto> showFlowerBloom(){

        User user = authUtil.getUserByAuthentication();
        Family family = user.getFamily();

        List<Flower> flower = flowerRepository.findFullBloom(family.getFamilyId());
        List<FlowerDto> bloomFlower = new ArrayList<>();

        for(Flower f : flower){
            FlowerDto flowerDto = FlowerDto.builder()
                    .flowerBloomDate(f.getFlowerBloomDate())
                    .flowerColor(f.getFlowerColor())
                    .flowerKind(f.getFlowerKind())
                    .build();

            bloomFlower.add(flowerDto);
        }
        return bloomFlower;

    }
}
