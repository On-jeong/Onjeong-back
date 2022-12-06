package com.example.onjeong.anniversary.service;

import com.example.onjeong.anniversary.domain.Anniversary;
import com.example.onjeong.anniversary.domain.AnniversaryType;
import com.example.onjeong.anniversary.dto.AnniversaryDto;
import com.example.onjeong.anniversary.exception.AnniversaryNotExistException;
import com.example.onjeong.error.ErrorCode;
import com.example.onjeong.family.domain.Family;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.anniversary.dto.AnniversaryRegisterDto;
import com.example.onjeong.anniversary.repository.AnniversaryRepository;
import com.example.onjeong.user.repository.UserRepository;
import com.example.onjeong.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnniversaryService {
    private final AnniversaryRepository anniversaryRepository;
    private final AuthUtil authUtil;

    //월별 모든 특수일정 가져오기
    @Transactional
    public List<AnniversaryDto> getAllAnniversaryOfMonth(final LocalDate anniversaryDate){
        final User loginUser= authUtil.getUserByAuthentication();
        final Family family= loginUser.getFamily();
        LocalDate start= anniversaryDate.withDayOfMonth(1);
        final LocalDate end= anniversaryDate.withDayOfMonth(anniversaryDate.lengthOfMonth());
        final List<AnniversaryDto> result= new ArrayList<>();
        while(!start.isAfter(end)){
            for(Anniversary a:anniversaryRepository.findByAnniversaryDate(start,family.getFamilyId())){ //해당 패밀리만 가져오는지 체크
                final AnniversaryDto anniversaryDto= AnniversaryDto.builder()
                        .anniversaryId(a.getAnniversaryId())
                        .anniversaryContent(a.getAnniversaryContent())
                        .anniversaryType(a.getAnniversaryType())
                        .anniversaryDate(a.getAnniversaryDate())
                        .build();
                result.add(anniversaryDto);
            }
            start= start.plus(1, ChronoUnit.DAYS);
        }
        return result;
    }

    //해당 일의 특수일정 가져오기
    @Transactional
    public List<AnniversaryDto> getAnniversaryOfDay(final LocalDate anniversaryDate){
        final User loginUser= authUtil.getUserByAuthentication();
        final Family family=loginUser.getFamily();
        final List<AnniversaryDto> result= new ArrayList<>();
        final List<Anniversary> anniversaries= anniversaryRepository.findAllByAnniversaryDateAndFamily(anniversaryDate,family);
        for(Anniversary a: anniversaries){
            final AnniversaryDto anniversaryDto= AnniversaryDto.builder()
                    .anniversaryId(a.getAnniversaryId())
                    .anniversaryContent(a.getAnniversaryContent())
                    .anniversaryType(a.getAnniversaryType())
                    .anniversaryDate(a.getAnniversaryDate())
                    .build();
            result.add(anniversaryDto);
        }
        return result;
    }

    //해당 일의 특수일정 등록하기
    @Transactional
    public void registerAnniversary(final LocalDate anniversaryDate, final AnniversaryRegisterDto anniversaryRegisterDto){
        final User loginUser= authUtil.getUserByAuthentication();
        final String getAnniversaryType= anniversaryRegisterDto.getAnniversaryType();
        if(getAnniversaryType.equals("ANNIVERSARY")) {
            final Anniversary anniversary = Anniversary.builder()
                    .anniversaryDate(anniversaryDate)
                    .anniversaryContent(anniversaryRegisterDto.getAnniversaryContent())
                    .anniversaryType(AnniversaryType.ANNIVERSARY)
                    .family(loginUser.getFamily())
                    .build();
            anniversaryRepository.save(anniversary);
        }
        if(getAnniversaryType.equals("SPECIAL_SCHEDULE")) {
            final Anniversary anniversary = Anniversary.builder()
                    .anniversaryDate(anniversaryDate)
                    .anniversaryContent(anniversaryRegisterDto.getAnniversaryContent())
                    .anniversaryType(AnniversaryType.SPECIAL_SCHEDULE)
                    .family(loginUser.getFamily())
                    .build();
            anniversaryRepository.save(anniversary);
        }
    }

    //해당 일의 특수일정 삭제하기
    @Transactional
    public void deleteAnniversary(final Long anniversaryId){
        final User loginUser= authUtil.getUserByAuthentication();
        final Family family=loginUser.getFamily();
        anniversaryRepository.deleteByAnniversaryIdAndFamily(anniversaryId, family);
    }
}