package com.example.onjeong.board;

import com.example.onjeong.board.domain.Board;
import com.example.onjeong.board.repository.BoardRepository;
import com.example.onjeong.family.domain.Family;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.util.BoardUtils;
import com.example.onjeong.util.FamilyUtils;
import com.example.onjeong.util.UserUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    void 오늘의일기_여러개_삭제(){
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
        assertThat(boardRepository.findAllByUser(user).size()).isEqualTo(0);
    }

}
