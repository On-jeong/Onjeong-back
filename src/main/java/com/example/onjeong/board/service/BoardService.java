package com.example.onjeong.board.service;


import com.example.onjeong.S3.S3Uploader;
import com.example.onjeong.board.domain.Board;
import com.example.onjeong.board.dto.BoardDto;
import com.example.onjeong.board.exception.BoardNotExistException;
import com.example.onjeong.board.exception.BoardWriterNotSameException;
import com.example.onjeong.error.ErrorCode;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.board.repository.BoardRepository;
import com.example.onjeong.user.exception.UserNotExistException;
import com.example.onjeong.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        User user= userRepository.findByUserNickname(authentication.getName())
                .orElseThrow(()-> new UserNotExistException("login user not exist", ErrorCode.USER_NOTEXIST));
        List<Board> boards= boardRepository.findAllByBoardDateAndFamily(boardDate,user.getFamily())
                .orElseThrow(()-> new BoardNotExistException("boards not exist", ErrorCode.BOARD_NOTEXIST));
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
    public Board boardRegister(final LocalDate boardDate, final MultipartFile multipartFile, final String boardContent){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User user=userRepository.findByUserNickname(authentication.getName())
                .orElseThrow(()-> new UserNotExistException("login user not exist", ErrorCode.USER_NOTEXIST));
        if(multipartFile == null) {
            final Board board=Board.builder()
                    .boardContent(boardContent)
                    .boardImageUrl(null)
                    .boardDate(boardDate)
                    .user(user)
                    .family(user.getFamily())
                    .build();
            return boardRepository.save(board);
        }
        else {
            final Board board=Board.builder()
                    .boardContent(boardContent)
                    .boardImageUrl(s3Uploader.upload(multipartFile, "board"))
                    .boardDate(boardDate)
                    .user(user)
                    .family(user.getFamily())
                    .build();
            return boardRepository.save(board);
        }
    }

    //오늘의 기록 한개 가져오기
    @Transactional
    public BoardDto boardGet(final Long boardId){
        Board board= boardRepository.findByBoardId(boardId)
                .orElseThrow(()-> new BoardNotExistException("board not exist", ErrorCode.BOARD_NOTEXIST));
        return BoardDto.builder()
                .boardId(board.getBoardId())
                .boardContent(board.getBoardContent())
                .boardImageUrl(board.getBoardImageUrl())
                .userStatus(board.getUser().getUserStatus())
                .build();
    }

    //오늘의 기록 수정하기
    @Transactional
    public void boardModify(final Long boardId, final MultipartFile multipartFile, final String boardContent){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User user= userRepository.findByUserNickname(authentication.getName())
                .orElseThrow(()-> new UserNotExistException("login user not exist", ErrorCode.USER_NOTEXIST));
        Board board= boardRepository.findByBoardId(boardId)
                .orElseThrow(()-> new BoardNotExistException("board not exist", ErrorCode.BOARD_NOTEXIST));
        if(user==board.getUser()){
            board.updateBoardContent(boardContent);
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
        }
        else throw new BoardWriterNotSameException("board writer not same",ErrorCode.BOARD_WRITER_NOT_SAME);
    }

    //오늘의 기록 삭제하기
    @Transactional
    public void boardRemove(final Long boardId){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User user=userRepository.findByUserNickname(authentication.getName())
                .orElseThrow(()-> new UserNotExistException("login user not exist", ErrorCode.USER_NOTEXIST));
        String deletedImage= boardRepository.findByBoardId(boardId)
                .orElseThrow(()-> new BoardNotExistException("deletedImage not exist", ErrorCode.BOARD_NOTEXIST)).getBoardImageUrl();
        if(boardRepository.deleteByBoardIdAndUser(boardId,user).equals("1")) {
            if(deletedImage!=null) s3Uploader.deleteFile(deletedImage.substring(AWS_S3_BUCKET_URL.length()));
        }
    }
}
