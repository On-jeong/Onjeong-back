package com.example.onjeong.board;

import com.example.onjeong.board.domain.Board;
import com.example.onjeong.board.repository.BoardRepository;
import com.example.onjeong.family.domain.Family;
import com.example.onjeong.family.repository.FamilyRepository;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.util.BoardUtils;
import com.example.onjeong.util.FamilyUtils;
import com.example.onjeong.util.UserUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private FamilyRepository familyRepository;


    private final Long familyId= 1L;



    @Test
    void 오늘의기록모두조회_날짜에따라(){
        //given
        final LocalDate boardDate= LocalDate.of(2022,12,03);
        final Family family= familyRepository.findById(familyId).get();


        //when
        List<Board> boards= boardRepository.findAllByBoardDateAndFamily(boardDate, family);


        //then
        assertThat(boards.size()).isEqualTo(3);
    }


    @Test
    void 오늘의기록_한개_저장(){
        //given
        final Family family= FamilyUtils.getRandomFamily();
        final User user= UserUtils.getRandomUser(family);
        final Board board= BoardUtils.getRandomBoard(family, user);


        //when
        final Board savedBoard= boardRepository.save(board);


        //then
        assertThat(savedBoard.getBoardId()).isEqualTo(board.getBoardId());
        assertThat(savedBoard.getBoardContent()).isEqualTo(board.getBoardContent());
        assertThat(savedBoard.getBoardDate()).isEqualTo(board.getBoardDate());
        assertThat(savedBoard.getBoardImageUrl()).isEqualTo(board.getBoardImageUrl());
    }


    @Test
    void 오늘의기록_한개_조회(){
        //given
        final Long boardId= 1L;


        //when
        final Board board= boardRepository.findByBoardId(boardId).get();


        //then
        assertThat(board.getBoardId()).isEqualTo(1L);
        assertThat(board.getBoardDate()).isEqualTo("2022-12-03");
    }


    @Test
    void 오늘의기록_한개_삭제(){
        //given
        final Long boardId= 1L;
        final User user= boardRepository.findByBoardId(boardId).get().getUser();


        //when
        boardRepository.deleteByBoardIdAndUser(boardId, user);


        //then

    }


    @Test
    void 오늘의기록_여러개_삭제(){
        //given
        final Family family= FamilyUtils.getRandomFamily();
        final User user= UserUtils.getRandomUser(family);
        final List<Board> boards= new ArrayList<>();

        for(int i=0;i<3;i++){
            final Board board= BoardUtils.getRandomBoard(family, user);
            boards.add(board);
        }
        boardRepository.saveAll(boards);


        //when
        boardRepository.deleteAllByUser(user);


        //then

    }

}