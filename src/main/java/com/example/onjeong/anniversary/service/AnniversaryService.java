package com.example.onjeong.anniversary.service;

import com.example.onjeong.anniversary.domain.Anniversary;
import com.example.onjeong.anniversary.domain.AnniversaryType;
import com.example.onjeong.anniversary.dto.AnniversaryDto;
import com.example.onjeong.error.ErrorCode;
import com.example.onjeong.family.domain.Family;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.anniversary.dto.AnniversaryModifyDto;
import com.example.onjeong.anniversary.dto.AnniversaryRegisterDto;
import com.example.onjeong.anniversary.repository.AnniversaryRepository;
import com.example.onjeong.user.exception.UserNotExistException;
import com.example.onjeong.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserRepository userRepository;

    //월별 모든 특수일정 가져오기
    @Transactional
    public List<AnniversaryDto> allAnniversaryGet(final LocalDate anniversaryDate){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User user= userRepository.findByUserNickname(authentication.getName())
                .orElseThrow(()-> new UserNotExistException("login user not exist", ErrorCode.USER_NOTEXIST));
        Family family= user.getFamily();

        LocalDate start= anniversaryDate.withDayOfMonth(1);
        LocalDate end= anniversaryDate.withDayOfMonth(anniversaryDate.lengthOfMonth());
        List<AnniversaryDto> result= new ArrayList<>();
        while(!start.isAfter(end)){
            for(Anniversary a:anniversaryRepository.findByAnniversaryDate(start,family.getFamilyId())
                    .orElseThrow(()-> new UserNotExistException("anniversary not exist", ErrorCode.USER_NOTEXIST))){ //해당 패밀리만 가져오는지 체크
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
    public List<AnniversaryDto> anniversaryGet(final LocalDate anniversaryDate){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User user=userRepository.findByUserNickname(authentication.getName())
                .orElseThrow(()-> new UserNotExistException("login user not exist", ErrorCode.USER_NOTEXIST));
        Family family=user.getFamily();

        final List<AnniversaryDto> result= new ArrayList<>();
        final List<Anniversary> anniversaries= anniversaryRepository.findAllByAnniversaryDateAndFamily(anniversaryDate,family).get();
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
    public String anniversaryRegister(final LocalDate anniversaryDate, final AnniversaryRegisterDto anniversaryRegisterDto){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User user=userRepository.findByUserNickname(authentication.getName())
                .orElseThrow(()-> new UserNotExistException("login user not exist", ErrorCode.USER_NOTEXIST));

        final Anniversary anniversary;
        if(anniversaryRegisterDto.getAnniversaryType().equals("ANNIVERSARY")) {
            anniversary = Anniversary.builder()
                    .anniversaryDate(anniversaryDate)
                    .anniversaryContent(anniversaryRegisterDto.getAnniversaryContent())
                    .anniversaryType(AnniversaryType.ANNIVERSARY)
                    .family(user.getFamily())
                    .build();
        }
        else {
            anniversary = Anniversary.builder()
                    .anniversaryDate(anniversaryDate)
                    .anniversaryContent(anniversaryRegisterDto.getAnniversaryContent())
                    .anniversaryType(AnniversaryType.SPECIAL_SCHEDULE)
                    .family(user.getFamily())
                    .build();
        }
        anniversaryRepository.save(anniversary);
        return "true";
    }

    //해당 일의 특수일정 수정하기
    @Transactional
    public String anniversaryModify(final Long anniversaryId, final AnniversaryModifyDto anniversaryModifyDto){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User user=userRepository.findByUserNickname(authentication.getName())
                .orElseThrow(()-> new UserNotExistException("login user not exist", ErrorCode.USER_NOTEXIST));
        Family family=user.getFamily();

        Anniversary anniversary= anniversaryRepository.findByAnniversaryIdAndFamily(anniversaryId,family)
                .orElseThrow(()-> new UserNotExistException("anniversary not exist", ErrorCode.USER_NOTEXIST));
        anniversary.updateAnniversaryContent(anniversaryModifyDto.getAnniversaryContent());
        if(anniversaryModifyDto.getAnniversaryType().equals("ANNIVERSARY")) anniversary.updateAnniversaryType(AnniversaryType.ANNIVERSARY);
        else anniversary.updateAnniversaryType(AnniversaryType.SPECIAL_SCHEDULE);
        anniversary.updateAnniversaryDate(anniversaryModifyDto.getAnniversaryDate());       //변경 x
        return "true";
    }

    //해당 일의 특수일정 삭제하기
    @Transactional
    public String anniversaryRemove(final Long anniversaryId){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User user=userRepository.findByUserNickname(authentication.getName())
                .orElseThrow(()-> new UserNotExistException("login user not exist", ErrorCode.USER_NOTEXIST));
        Family family=user.getFamily();

        if(anniversaryRepository.deleteByAnniversaryIdAndFamily(anniversaryId,family).equals("1")) return "true";
        else return "false";
    }
}