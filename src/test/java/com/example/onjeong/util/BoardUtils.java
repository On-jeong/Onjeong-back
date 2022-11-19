package com.example.onjeong.util;

import com.example.onjeong.board.domain.Board;
import com.example.onjeong.family.domain.Family;
import com.example.onjeong.profile.domain.Expression;
import com.example.onjeong.profile.domain.Profile;
import com.example.onjeong.user.domain.User;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;

public class BoardUtils {

    public static Board getRandomBoard(Family family, User user){
        final Long boardId= 50L;
        final String boardContent= RandomStringUtils.random(8, true, true);
        final String boardImageUrl= RandomStringUtils.random(8, true, true);
        final LocalDate boardDate= LocalDate.now();
        return getBoard(boardId, boardContent, boardImageUrl, boardDate, family, user);
    }


    public static Board getBoard(Long boardId, String boardContent, String boardImageUrl, LocalDate boardDate,
                                 Family family, User user){
        return Board.builder()
                .boardId(boardId)
                .boardContent(boardContent)
                .boardImageUrl(boardImageUrl)
                .boardDate(boardDate)
                .family(family)
                .user(user)
                .build();
    }
}
