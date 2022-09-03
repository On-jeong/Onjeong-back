package com.example.onjeong.home.controller;

import com.example.onjeong.home.domain.CoinHistoryType;
import com.example.onjeong.home.domain.FlowerKind;
import com.example.onjeong.home.dto.CoinHistoryDto;
import com.example.onjeong.home.dto.FlowerDto;
import com.example.onjeong.home.service.CoinService;
import com.example.onjeong.home.service.FlowerService;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags="Home")
@RequiredArgsConstructor
@RestController
public class HomeController {

    private final FlowerService flowerService;
    private final CoinService coinService;

    @ApiOperation(value = "패밀리 레벨에 따른 꽃 종류")
    @GetMapping("/flowers")
    public ResponseEntity<FlowerDto> flowers() {
        return ResponseEntity.ok(flowerService.showFlower());
    }

    @ApiOperation(value = "만개한 꽃장 보여주기")
    @GetMapping("/flowers-bloom")
    public ResponseEntity<List<FlowerDto>> flowersBloom() {
        return ResponseEntity.ok(flowerService.showFlowerBloom());
    }

    @ApiOperation(value = "패밀리 코인 적립 내역")
    @GetMapping("/histories")
    public ResponseEntity<List<CoinHistoryDto>> histories() {

        return ResponseEntity.ok(coinService.coinHistoryList());
    }

    @ApiOperation(value = "패밀리 코인 보여주기")
    @GetMapping("/coins")
    public ResponseEntity<Integer> coins() {
        return ResponseEntity.ok(coinService.coinShow());
    }

    @ApiOperation(value = "랜덤하게 데일리 코인 지급")
    @PostMapping("/coins-random")
    public ResponseEntity<Integer> coinsRandom() throws FirebaseMessagingException {
        int randAmount = (int) (Math.random() * (100 - 10 + 1)) + 10; // 10~100 사이 랜덤
        CoinHistoryDto coinRand = coinService.coinSave(CoinHistoryType.RAND, randAmount);
        return ResponseEntity.ok(coinRand.getAmount());
    }
}
