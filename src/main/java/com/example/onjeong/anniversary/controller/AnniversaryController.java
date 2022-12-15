package com.example.onjeong.anniversary.controller;

import com.example.onjeong.anniversary.dto.AnniversaryDto;
import com.example.onjeong.anniversary.dto.AnniversaryRegisterDto;
import com.example.onjeong.anniversary.service.AnniversaryService;
import com.example.onjeong.result.ResultCode;
import com.example.onjeong.result.ResultResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Api(tags="Anniversary")
@RequiredArgsConstructor
@RestController
@Log4j2
public class AnniversaryController {
    private final AnniversaryService anniversaryService;

    @ApiOperation(value="월별 모든 특수일정 가져오기")
    @GetMapping(value = "/months/anniversaries/{anniversaryDate}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> getAllAnniversaryOfMonth(@PathVariable("anniversaryDate") String anniversaryDate){
        List<AnniversaryDto> data= anniversaryService.getAllAnniversaryOfMonth(LocalDate.parse(anniversaryDate, DateTimeFormatter.ISO_DATE));
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_ALL_ANNIVERSARY_SUCCESS,data));
    }

    @ApiOperation(value="해당 일의 특수일정 가져오기")
    @GetMapping(value = "/days/anniversaries/{anniversaryDate}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> getAnniversaryOfDay(@PathVariable("anniversaryDate") String anniversaryDate){
        List<AnniversaryDto> data= anniversaryService.getAnniversaryOfDay(LocalDate.parse(anniversaryDate, DateTimeFormatter.ISO_DATE));
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_ANNIVERSARY_SUCCESS,data));
    }

    @ApiOperation(value="해당 일의 특수일정 등록하기")
    @PostMapping(value = "/days/anniversaries/{anniversaryDate}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> registerAnniversary(@PathVariable("anniversaryDate") String anniversaryDate, @RequestBody AnniversaryRegisterDto anniversaryRegisterDto){
        anniversaryService.registerAnniversary(LocalDate.parse(anniversaryDate, DateTimeFormatter.ISO_DATE),anniversaryRegisterDto);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.REGISTER_ANNIVERSARY_SUCCESS));
    }

    @ApiOperation(value="해당 일의 특수일정 삭제하기")
    @DeleteMapping(value = "/days/anniversaries/{anniversaryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> deleteAnniversary(@PathVariable("anniversaryId") Long anniversaryId){
        anniversaryService.deleteAnniversary(anniversaryId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.DELETE_ANNIVERSARY_SUCCESS));
    }
}
