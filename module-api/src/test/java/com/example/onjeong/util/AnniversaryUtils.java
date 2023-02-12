package com.example.onjeong.util;

import com.example.onjeong.anniversary.domain.Anniversary;
import com.example.onjeong.anniversary.domain.AnniversaryType;
import com.example.onjeong.family.domain.Family;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;

public class AnniversaryUtils {

    public static Anniversary getRandomAnniversary(Family family){
        final Long anniversaryId= 50L;
        final String anniversaryContent= RandomStringUtils.random(8, true, true);
        final AnniversaryType anniversaryType= AnniversaryType.ANNIVERSARY;
        final LocalDate anniversaryDate= LocalDate.now();
        return getAnniversary(anniversaryId, anniversaryContent, anniversaryType, anniversaryDate, family);
    }


    public static Anniversary getAnniversary(Long anniversaryId, String anniversaryContent, AnniversaryType anniversaryType,
                                             LocalDate anniversaryDate, Family family){
        return Anniversary.builder()
                .anniversaryId(anniversaryId)
                .anniversaryContent(anniversaryContent)
                .anniversaryType(anniversaryType)
                .anniversaryDate(anniversaryDate)
                .family(family)
                .build();
    }
}