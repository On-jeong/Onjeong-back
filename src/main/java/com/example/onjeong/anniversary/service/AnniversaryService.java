package com.example.onjeong.anniversary.service;

import com.example.onjeong.anniversary.domain.Anniversary;
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
    private final FamilyRepository familyRepository;
    private final UserRepository userRepository;

    //월별 모든 특수일정 가져오기
    @Transactional
    public List<Map<LocalDate,Anniversary>> allAnniversaryGet(final LocalDate anniversaryDate){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user=userRepository.findByUserNickname(authentication.getName());
        Long familyId=user.get().getFamily().getFamilyId();

        LocalDate start= anniversaryDate.withDayOfMonth(1);
        LocalDate end= anniversaryDate.withDayOfMonth(anniversaryDate.lengthOfMonth());
        List<Map<LocalDate,Anniversary>> result= new ArrayList<>();
        for(Anniversary a:anniversaryRepository.findAllByAnniversaryDateBetween(start,end).get()){
            if(familyId==a.getFamily().getFamilyId()){
                Map<LocalDate,Anniversary> map=new HashMap<>();
                map.put(a.getAnniversaryDate(),a);
                result.add(map);
            }
        }
        return result;
    }

    //해당 일의 특수일정 가져오기
    @Transactional
    public List<Anniversary> anniversaryGet(final LocalDate anniversaryDate){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user=userRepository.findByUserNickname(authentication.getName());
        Family family=user.get().getFamily();

        return anniversaryRepository.findAllByAnniversaryDateAndFamily(anniversaryDate,family).get();
    }

    //해당 일의 특수일정 등록하기
    @Transactional
    public Anniversary anniversaryRegister(final LocalDate anniversaryDate, final AnniversaryRegisterDto anniversaryRegisterDto){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user=userRepository.findByUserNickname(authentication.getName());

        final Anniversary anniversary= Anniversary.builder()
                .anniversaryDate(anniversaryDate)
                .anniversaryContent(anniversaryRegisterDto.getAnniversaryContent())
                .family(user.get().getFamily())
                .build();

        return anniversaryRepository.save(anniversary);
    }

    //해당 일의 특수일정 수정하기
    @Transactional
    public Anniversary anniversaryModify(final LocalDate anniversaryDate, final Long anniversaryId, final AnniversaryModifyDto anniversaryModifyDto){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user=userRepository.findByUserNickname(authentication.getName());
        Family family=user.get().getFamily();

        Optional<Anniversary> anniversary= anniversaryRepository.findByAnniversaryDateAndAnniversaryIdAndFamily(anniversaryDate,anniversaryId,family);
        anniversary.get().updateAnniversaryContent(anniversaryModifyDto.getAnniversaryContent());
        anniversary.get().updateAnniversaryDate(anniversaryDate);       //변경 x
        return anniversaryRepository.save(anniversary.get());
    }

    //해당 일의 특수일정 삭제하기
    @Transactional
    public Boolean anniversaryRemove(final LocalDate anniversaryDate, final Long anniversaryId){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user=userRepository.findByUserNickname(authentication.getName());
        Family family=user.get().getFamily();

        if(anniversaryRepository.deleteByAnniversaryDateAndAnniversaryIdAndFamily(anniversaryDate,anniversaryId,family).equals("1")) return true;
        else return false;
    }
}
