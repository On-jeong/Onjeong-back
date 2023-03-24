package com.example.onjeong.coin.service;

import com.example.onjeong.coin.domain.CoinHistory;
import com.example.onjeong.coin.domain.CoinHistoryType;
import com.example.onjeong.coin.dto.CoinHistoryDto;
import com.example.onjeong.coin.repository.CoinHistoryRepository;
import com.example.onjeong.family.domain.Family;
import com.example.onjeong.home.domain.Flower;
import com.example.onjeong.home.domain.FlowerColor;
import com.example.onjeong.home.domain.FlowerKind;
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
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CoinService {

    private final CoinHistoryRepository coinHistoryRepository;
    private final FlowerRepository flowerRepository;
    private final AuthUtil authUtil;

    @Transactional
    public void coinSave(CoinHistoryType coinHistoryType, int amount) {

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
        coinHistoryRepository.save(coinHistory);

        // 변경 된 코인 수에 따라 꽃 상태 변경
        Flower flower = flowerRepository.findBlooming(family.getFamilyId());

        int level = flower.getFlowerLevel();
        if (level <= 10) {
            if ((family.getFamilyCoin() / 1000) != 0) {
                // 레벨을 상승시켜야 할 정도로 코인이 쌓였을 경우
                family.updateCoin(-1000);
                CoinHistory coinHistory2 = CoinHistory.builder()
                        .coinAmount(-1000)
                        .coinHistoryType(CoinHistoryType.USED)
                        .coinHistoryDate(LocalDateTime.now())
                        .coinFlower(flower.getFlowerLevel() + 1)
                        .user(user)
                        .family(family)
                        .build();
                coinHistoryRepository.save(coinHistory2);
                flower.levelUp();
            }
        }

        if (flower.getFlowerLevel() == 10) {
            // 새로운 꽃 추가
            List<FlowerKind> flowerKinds = FindRandomFlowerKind(family.getFamilyId());
            List<FlowerColor> flowerColors = FindRandomFlowerColor(family.getFamilyId());
            Flower newFlower = Flower.builder()
                    .flowerBloom(false)
                    .flowerKind(flowerKinds.get(new Random().nextInt(flowerKinds.size())))
                    .flowerColor(flowerColors.get(new Random().nextInt(flowerColors.size())))
                    .flowerLevel(1)
                    .family(family)
                    .build();

            flowerRepository.save(newFlower);
        }
    }

    public List<CoinHistoryDto> coinHistoryList(Pageable pageable) {

        User user = authUtil.getUserByAuthentication();
        Family family = user.getFamily();

        List<CoinHistory> coinHistoryList = coinHistoryRepository.findByFamily(pageable, family.getFamilyId()).toList();
        List<CoinHistoryDto> coinHistoryDtoList = new ArrayList<>();

        for (CoinHistory c : coinHistoryList) {
            CoinHistoryDto coinHistoryDto = CoinHistoryDto.builder()
                    .amount(c.getCoinAmount())
                    .type(c.getCoinHistoryType())
                    .user(c.getUser().getUserStatus())
                    .date(c.getCoinHistoryDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")))
                    .before(c.getCoinFlower() - 1)
                    .after(c.getCoinFlower() - 1)
                    .build();

            coinHistoryDtoList.add(coinHistoryDto);
        }

        return coinHistoryDtoList;
    }

    public int coinShow() {

        User user = authUtil.getUserByAuthentication();
        Family family = user.getFamily();

        return family.getFamilyCoin();
    }

    // 최대한 안 쓴 종류 반환
    public List<FlowerKind> FindRandomFlowerKind(Long familyId) {

        List<Flower> flowerFullBlooms = flowerRepository.findFullBloom(familyId);
        List<FlowerKind> flowerKinds = new ArrayList<>();
        // 꽃 종류가 3개니까 0~2사이
        int[] flowerKind = {0, 0, 0};

        for (Flower f : flowerFullBlooms) {
            flowerKind[f.getFlowerKind().ordinal()]++;
        }

        int min = Arrays.stream(flowerKind).min().getAsInt();
        int[] answer = IntStream.range(0, flowerKind.length).filter(i -> flowerKind[i] == min).toArray();

        for (int i : answer) {
            flowerKinds.add(FlowerKind.values()[i]);
        }

        return flowerKinds;
    }

    // 최대한 안 쓴 색상 반환
    public List<FlowerColor> FindRandomFlowerColor(Long familyId) {

        List<Flower> flowerFullBlooms = flowerRepository.findFullBloom(familyId);
        List<FlowerColor> flowerColors = new ArrayList<>();
        // 꽃 색상이 8개니까 0~7사이
        int[] flowerColor = {0, 0, 0, 0, 0, 0, 0, 0};

        for (Flower f : flowerFullBlooms) {
            flowerColor[f.getFlowerColor().ordinal()]++;
        }

        int min = Arrays.stream(flowerColor).min().getAsInt();
        int[] answer = IntStream.range(0, flowerColor.length).filter(i -> flowerColor[i] == min).toArray();

        for (int i : answer) {
            flowerColors.add(FlowerColor.values()[i]);
        }

        return flowerColors;
    }
}