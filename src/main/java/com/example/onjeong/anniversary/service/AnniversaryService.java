package com.example.onjeong.anniversary.service;

import com.example.onjeong.anniversary.domain.Anniversary;
import com.example.onjeong.anniversary.domain.AnniversaryType;
import com.example.onjeong.anniversary.dto.AnniversaryDto;
import com.example.onjeong.family.domain.Family;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.anniversary.dto.AnniversaryModifyDto;
import com.example.onjeong.anniversary.dto.AnniversaryRegisterDto;
import com.example.onjeong.anniversary.repository.AnniversaryRepository;
import com.example.onjeong.family.repository.FamilyRepository;
import com.example.onjeong.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnniversaryService {
    private final AnniversaryRepository anniversaryRepository;
    private final UserRepository userRepository;

    //월별 모든 특수일정 가져오기
    @Transactional
    public List<Map<LocalDate, AnniversaryDto>> allAnniversaryGet(final LocalDate anniversaryDate){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user= userRepository.findByUserNickname(authentication.getName());
        Family family= user.get().getFamily();

        LocalDate start= anniversaryDate.withDayOfMonth(1);
        LocalDate end= anniversaryDate.withDayOfMonth(anniversaryDate.lengthOfMonth());
        List<Map<LocalDate,AnniversaryDto>> result= new ArrayList<>();
        for(Anniversary a:anniversaryRepository.findAllByAnniversaryDateBetweenAndFamily(start,end, family).get()){ //해당 패밀리만 가져오는지 체크
            Map<LocalDate,AnniversaryDto> map=new HashMap<>();
            final AnniversaryDto anniversaryDto= AnniversaryDto.builder()
                    .anniversaryId(a.getAnniversaryId())
                    .anniversaryContent(a.getAnniversaryContent())
                    .anniversaryType(a.getAnniversaryType())
                    .build();
            map.put(a.getAnniversaryDate(),anniversaryDto);
            result.add(map);
        }
        return result;
    }

    //해당 일의 특수일정 가져오기
    @Transactional
    public List<AnniversaryDto> anniversaryGet(final LocalDate anniversaryDate){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user=userRepository.findByUserNickname(authentication.getName());
        Family family=user.get().getFamily();

        List<AnniversaryDto> result= new ArrayList<>();
        List<Anniversary> anniversaries= anniversaryRepository.findAllByAnniversaryDateAndFamily(anniversaryDate,family).get();
        for(Anniversary a: anniversaries){
            final AnniversaryDto anniversaryDto= AnniversaryDto.builder()
                    .anniversaryId(a.getAnniversaryId())
                    .anniversaryContent(a.getAnniversaryContent())
                    .anniversaryType(a.getAnniversaryType())
                    .build();
            result.add(anniversaryDto);
        }
        return result;
    }

    //해당 일의 특수일정 등록하기
    @Transactional
    public String anniversaryRegister(final LocalDate anniversaryDate, final AnniversaryRegisterDto anniversaryRegisterDto){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user=userRepository.findByUserNickname(authentication.getName());

        if(anniversaryRegisterDto.getAnniversaryType().equals("ANNIVERSARY")) {
            final Anniversary anniversary= Anniversary.builder()
                    .anniversaryDate(anniversaryDate)
                    .anniversaryContent(anniversaryRegisterDto.getAnniversaryContent())
                    .anniversaryType(AnniversaryType.ANNIVERSARY)
                    .family(user.get().getFamily())
                    .build();
            anniversaryRepository.save(anniversary);
            return "true";
        }
        else {
            final Anniversary anniversary = Anniversary.builder()
                    .anniversaryDate(anniversaryDate)
                    .anniversaryContent(anniversaryRegisterDto.getAnniversaryContent())
                    .anniversaryType(AnniversaryType.SPECIAL_SCHEDULE)
                    .family(user.get().getFamily())
                    .build();
            anniversaryRepository.save(anniversary);
            return "true";
        }
    }

    //해당 일의 특수일정 수정하기
    @Transactional
    public String anniversaryModify(final Long anniversaryId, final AnniversaryModifyDto anniversaryModifyDto){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user=userRepository.findByUserNickname(authentication.getName());
        Family family=user.get().getFamily();

        Optional<Anniversary> anniversary= anniversaryRepository.findByAnniversaryIdAndFamily(anniversaryId,family);
        anniversary.get().updateAnniversaryContent(anniversaryModifyDto.getAnniversaryContent());
        if(anniversaryModifyDto.getAnniversaryType().equals("ANNIVERSARY")) anniversary.get().updateAnniversaryType(AnniversaryType.ANNIVERSARY);
        else anniversary.get().updateAnniversaryType(AnniversaryType.SPECIAL_SCHEDULE);
        anniversary.get().updateAnniversaryDate(anniversaryModifyDto.getAnniversaryDate());       //변경 x
        return "true";
    }

    //해당 일의 특수일정 삭제하기
    @Transactional
<<<<<<< HEAD
    public Boolean anniversaryRemove(final LocalDate anniversaryDate, final Long anniversaryId){
=======
    public String anniversaryRemove(final Long anniversaryId){
>>>>>>> fb8fcf4d4e8117bf277d169b87f4490f67710f1a
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user=userRepository.findByUserNickname(authentication.getName());
        Family family=user.get().getFamily();

<<<<<<< HEAD
        if(anniversaryRepository.deleteByAnniversaryDateAndAnniversaryIdAndFamily(anniversaryDate,anniversaryId,family).equals("1")) return true;
        else return false;
=======
        if(anniversaryRepository.deleteByAnniversaryIdAndFamily(anniversaryId,family).equals("1")) return "true";
        else return "false";
>>>>>>> fb8fcf4d4e8117bf277d169b87f4490f67710f1a
    }
}
