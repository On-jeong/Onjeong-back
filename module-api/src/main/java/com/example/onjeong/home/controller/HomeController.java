package com.example.onjeong.home.controller;

import com.example.onjeong.coin.domain.CoinHistoryType;
import com.example.onjeong.coin.service.CoinService;
import com.example.onjeong.error.ErrorCode;
import com.example.onjeong.home.exception.RandCoinDuplicateException;
import com.example.onjeong.home.service.HomeService;
import com.example.onjeong.result.ResultCode;
import com.example.onjeong.result.ResultResponse;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags="Home")
@RequiredArgsConstructor
@RestController
public class HomeController {

    private final HomeService homeService;
    private final CoinService coinService;

    @ApiOperation(value = "패밀리 레벨에 따른 꽃 종류")
    @GetMapping("/flowers")
    public ResponseEntity<ResultResponse> flowers() {
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_FLOWER_SUCCESS, homeService.showFlower()));
    }

    @ApiOperation(value = "만개한 꽃장 보여주기")
    @GetMapping("/flowers-bloom")
    public ResponseEntity<ResultResponse> flowersBloom() {
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_BLOOM_SUCCESS, homeService.showFlowerBloom()));
    }

    @ApiOperation(value = "패밀리 코인 적립 내역")
    @GetMapping("/histories")
    public ResponseEntity<ResultResponse> histories(@PageableDefault(size=20, sort = "coin_history_id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_HISTORY_SUCCESS,coinService.coinHistoryList(pageable)));
    }

    @ApiOperation(value = "패밀리 코인 보여주기")
    @GetMapping("/coins")
    public ResponseEntity<ResultResponse> coins() {
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_COIN_SUCCESS,coinService.coinShow()));
    }

    @ApiOperation(value = "랜덤하게 데일리 코인 지급")
    @PostMapping("/coins-random")
    public ResponseEntity<ResultResponse> coinsRandom() throws FirebaseMessagingException {
        Pair<Boolean, Integer> randomResult = homeService.randomCoin();
        if(randomResult.getFirst()) coinService.coinSave(CoinHistoryType.RAND, randomResult.getSecond());
        else throw new RandCoinDuplicateException("random coin can accumulate once a day", ErrorCode.RAND_COIN_DUPLICATE);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.POST_COIN_SUCCESS,  randomResult.getSecond()));
    }
}