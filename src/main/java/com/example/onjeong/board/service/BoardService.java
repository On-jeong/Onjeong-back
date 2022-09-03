package com.example.onjeong.board.service;


import com.example.onjeong.S3.S3Uploader;
import com.example.onjeong.board.domain.Board;
import com.example.onjeong.board.dto.BoardDto;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.board.repository.BoardRepository;
import com.example.onjeong.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final S3Uploader s3Uploader;

    @Value("https://onjeong.s3.ap-northeast-2.amazonaws.com/")
    private String AWS_S3_BUCKET_URL;

    //오늘의 기록 모두 가져오기
    @Transactional
    public List<BoardDto> allBoardGet(final LocalDate boardDate){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User user=userRepository.findByUserNickname(authentication.getName()).get();
        List<Board> boards= boardRepository.findAllByBoardDateAndFamily(boardDate,user.getFamily()).get();
        final List<BoardDto> result= new ArrayList<>();
        for(Board b: boards){
            final BoardDto boardDto= BoardDto.builder()
                    .boardId(b.getBoardId())
                    .boardContent(b.getBoardContent())
                    .boardImageUrl(b.getBoardImageUrl())
                    .userStatus(b.getUser().getUserStatus())
                    .build();
            result.add(boardDto);
        }
        return result;
    }

    //오늘의 기록 작성하기
    @Transactional
    public String boardRegister(final LocalDate boardDate, final MultipartFile multipartFile, final String boardContent){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User user=userRepository.findByUserNickname(authentication.getName()).get();
        if(multipartFile == null) {
            final Board board=Board.builder()
                    .boardContent(boardContent)
                    .boardImageUrl(null)
                    .boardDate(boardDate)
                    .user(user)
                    .family(user.getFamily())
                    .build();
            boardRepository.save(board);
            return "true";
        }
        else {
            try {
                final Board board=Board.builder()
                        .boardContent(boardContent)
                        .boardImageUrl(s3Uploader.upload(multipartFile, "board"))
                        .boardDate(boardDate)
                        .user(user)
                        .family(user.getFamily())
                        .build();
                boardRepository.save(board);
                return "true";
            } catch (IOException e) {
                e.printStackTrace();
                return e.getMessage();
            }
        }
    }

    //오늘의 기록 한개 가져오기
    @Transactional
    public BoardDto boardGet(final Long boardId){
        Board board= boardRepository.findByBoardId(boardId).get();
        return BoardDto.builder()
                .boardId(board.getBoardId())
                .boardContent(board.getBoardContent())
                .boardImageUrl(board.getBoardImageUrl())
                .userStatus(board.getUser().getUserStatus())
                .build();
    }

    //오늘의 기록 수정하기
    @Transactional
    public String boardModify(final Long boardId, final MultipartFile multipartFile, final String boardContent, final LocalDate boardDate){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user= userRepository.findByUserNickname(authentication.getName());
        Board board= boardRepository.findByBoardId(boardId).get();
        if(user.get()==board.getUser()){
            board.updateBoardContent(boardContent);
            try {
                if(multipartFile==null && board.getBoardImageUrl()==null) board.updateBoardImageUrl(null);
                else if(multipartFile==null) {
                    s3Uploader.deleteFile(board.getBoardImageUrl().substring(AWS_S3_BUCKET_URL.length()));
                    board.updateBoardImageUrl(null);
                }
                else if(board.getBoardImageUrl()==null){
                    board.updateBoardImageUrl(s3Uploader.upload(multipartFile, "board"));
                }
                else{
                    s3Uploader.deleteFile(board.getBoardImageUrl().substring(AWS_S3_BUCKET_URL.length()));
                    board.updateBoardImageUrl(s3Uploader.upload(multipartFile, "board"));
                }
            } catch (IOException e) {
                e.printStackTrace();
                return e.getMessage();
            }
            board.updateBoardDate(boardDate);
            return "true";
        }
        else return "false";
    }

    //오늘의 기록 삭제하기
    @Transactional
    public String boardRemove(final Long boardId){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user=userRepository.findByUserNickname(authentication.getName());
        String deletedImage= boardRepository.findByBoardId(boardId).get().getBoardImageUrl();
        if(boardRepository.deleteByBoardIdAndUser(boardId,user.get()).equals("1")) {
            if(deletedImage!=null) s3Uploader.deleteFile(deletedImage.substring(AWS_S3_BUCKET_URL.length()));
            return "true";
        }
        else return "false";
    }
}
