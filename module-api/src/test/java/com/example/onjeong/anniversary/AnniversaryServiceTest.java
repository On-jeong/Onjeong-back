package com.example.onjeong.anniversary;

import com.example.onjeong.anniversary.domain.Anniversary;
import com.example.onjeong.anniversary.dto.AnniversaryDto;
import com.example.onjeong.anniversary.dto.AnniversaryRegisterDto;
import com.example.onjeong.anniversary.repository.AnniversaryRepository;
import com.example.onjeong.anniversary.service.AnniversaryService;
import com.example.onjeong.family.domain.Family;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.util.AuthUtil;
import com.example.onjeong.util.FamilyUtils;
import com.example.onjeong.util.UserUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnniversaryServiceTest {

    @InjectMocks
    private AnniversaryService anniversaryService;

    @Mock
    private AnniversaryRepository anniversaryRepository;

    @Mock
    private AuthUtil authUtil;



    @Test
    void 월별_모든_특수일정_가져오기(){
        //given
        final LocalDate anniversaryDate= LocalDate.of(2022,12,03);
        final Family family= FamilyUtils.getRandomFamily();
        final User user= UserUtils.getRandomUser(family);

        doReturn(user).when(authUtil).getUserByAuthentication();
        doReturn(Collections.emptyList()).when(anniversaryRepository).findByAnniversaryDate(any(LocalDate.class),any(Long.class));


        //when
        List<AnniversaryDto> result= anniversaryService.getAllAnniversaryOfMonth(anniversaryDate);


        //then
        assertThat(result.size()).isEqualTo(0);
        verify(anniversaryRepository,times(31)).findByAnniversaryDate(any(LocalDate.class),any(Long.class));
    }


    @Test
    void 해당_일의_특수일정_가져오기(){
        //given
        final LocalDate anniversaryDate= LocalDate.of(2022,12,03);
        final Family family= FamilyUtils.getRandomFamily();
        final User user= UserUtils.getRandomUser(family);

        doReturn(user).when(authUtil).getUserByAuthentication();
        doReturn(Collections.emptyList()).when(anniversaryRepository).findAllByAnniversaryDateAndFamily(any(LocalDate.class),any(Family.class));


        //when
        List<AnniversaryDto> result= anniversaryService.getAnniversaryOfDay(anniversaryDate);


        //then
        assertThat(result.size()).isEqualTo(0);
        verify(anniversaryRepository,times(1)).findAllByAnniversaryDateAndFamily(any(LocalDate.class),any(Family.class));
    }

    @Nested
    class 해당_일의_특수일정_등록하기{
        @Test
        void 기념일타입이_ANNIVERSARY_인경우(){
            //given
            final LocalDate anniversaryDate= LocalDate.of(2022,12,03);
            final AnniversaryRegisterDto anniversaryRegisterDto= anniversaryRegisterDto("birthday", "ANNIVERSARY");
            final Family family= FamilyUtils.getRandomFamily();
            final User user= UserUtils.getRandomUser(family);

            doReturn(user).when(authUtil).getUserByAuthentication();


            //when
            anniversaryService.registerAnniversary(anniversaryDate, anniversaryRegisterDto);


            //then
            assertThat(anniversaryRegisterDto.getAnniversaryType().equals("ANNIVERSARY")).isEqualTo(true);
            verify(anniversaryRepository,times(1)).save(any(Anniversary.class));
        }

        @Test
        void 기념일타입이_SPECIAL_SCHEDULE_인경우(){
            //given
            final LocalDate anniversaryDate= LocalDate.of(2022,12,03);
            final AnniversaryRegisterDto anniversaryRegisterDto= anniversaryRegisterDto("study day", "SPECIAL_SCHEDULE");
            final Family family= FamilyUtils.getRandomFamily();
            final User user= UserUtils.getRandomUser(family);

            doReturn(user).when(authUtil).getUserByAuthentication();


            //when
            anniversaryService.registerAnniversary(anniversaryDate, anniversaryRegisterDto);


            //then
            assertThat(anniversaryRegisterDto.getAnniversaryType().equals("SPECIAL_SCHEDULE")).isEqualTo(true);
            verify(anniversaryRepository,times(1)).save(any(Anniversary.class));
        }
    }


    @Test
    void 해당_일의_특수일정_삭제하기(){
        //given
        final Long anniversaryId= new Random().nextLong();
        final Family family= FamilyUtils.getRandomFamily();
        final User user= UserUtils.getRandomUser(family);

        doReturn(user).when(authUtil).getUserByAuthentication();


        //when
        anniversaryService.deleteAnniversary(anniversaryId);


        //then
        verify(anniversaryRepository,times(1)).deleteByAnniversaryIdAndFamily(anniversaryId, family);
    }



    private AnniversaryRegisterDto anniversaryRegisterDto(String anniversaryContent, String anniversaryType){
        return AnniversaryRegisterDto.builder()
                .anniversaryContent(anniversaryContent)
                .anniversaryType(anniversaryType)
                .build();
    }
}