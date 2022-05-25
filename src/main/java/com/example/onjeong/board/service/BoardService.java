package com.example.onjeong.board.service;


import com.example.onjeong.board.domain.Board;
import com.example.onjeong.family.domain.Family;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.board.dto.BoardModifyDto;
import com.example.onjeong.board.dto.BoardRegisterDto;
import com.example.onjeong.board.repository.BoardRepository;
import com.example.onjeong.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    //오늘의 기록 모두 가져오기
    @Transactional
    public List<Board> allBoardGet(final LocalDate boardDate){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user=userRepository.findByUserNickname(authentication.getName());
        Family family=user.get().getFamily();

        return boardRepository.findAllByBoardDateAndFamily(boardDate,family).get();
    }

    //오늘의 기록 작성하기
    @Transactional
    public Board boardRegister(final LocalDate boardDate,final BoardRegisterDto boardRegisterDto){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user=userRepository.findByUserNickname(authentication.getName());

        final Board board=Board.builder()
                .boardContent(boardRegisterDto.getBoardContent())
                .boardDate(boardDate)
                .user(user.get())
                .family(user.get().getFamily())
                .build();

        return boardRepository.save(board);
    }

    //오늘의 기록 한개 가져오기
    @Transactional
    public Board boardGet(final Long boardId){
        return boardRepository.findByBoardId(boardId).get();
    }

    //오늘의 기록 수정하기
    @Transactional
    public Board boardModify(final Long boardId, final BoardModifyDto boardModifyDto){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user=userRepository.findByUserNickname(authentication.getName());
        Board board=boardRepository.findByBoardId(boardId).get();
        if(user.get()==board.getUser()){
            board.updateBoardContent(boardModifyDto.getBoardContent());
            return boardRepository.save(board);
        }
        else return null;
    }

    //오늘의 기록 삭제하기
    @Transactional
    public String boardRemove(final Long boardId){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user=userRepository.findByUserNickname(authentication.getName());

        if(boardRepository.deleteByBoardIdAndUser(boardId,user.get()).equals("1")) return "true";
        else return "false";
    }
}
