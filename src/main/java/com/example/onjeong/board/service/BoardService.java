package com.example.onjeong.board.service;


import com.example.onjeong.S3.S3Uploader;
import com.example.onjeong.board.domain.Board;
import com.example.onjeong.board.dto.BoardDto;
import com.example.onjeong.board.exception.BoardNotExistException;
import com.example.onjeong.board.exception.BoardWriterNotSameException;
import com.example.onjeong.error.ErrorCode;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.board.repository.BoardRepository;
import com.example.onjeong.user.repository.UserRepository;
import com.example.onjeong.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;
    private final S3Uploader s3Uploader;
    private final AuthUtil authUtil;

    @Value("https://onjeong.s3.ap-northeast-2.amazonaws.com/")
    private String AWS_S3_BUCKET_URL;

    //오늘의 기록 모두 가져오기
    @Transactional
    public List<BoardDto> getAllBoard(final LocalDate boardDate){
        final User loginUser= authUtil.getUserByAuthentication();
        final List<Board> boards= boardRepository.findAllByBoardDateAndFamily(boardDate,loginUser.getFamily());
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
    public Board registerBoard(final LocalDate boardDate, final MultipartFile multipartFile, final String boardContent){
        final User loginUser= authUtil.getUserByAuthentication();
        if(multipartFile==null) {
            final Board board=Board.builder()
                    .boardContent(boardContent)
                    .boardImageUrl(null)
                    .boardDate(boardDate)
                    .user(loginUser)
                    .family(loginUser.getFamily())
                    .build();
            return boardRepository.save(board);
        }
        else {
            final Board board=Board.builder()
                    .boardContent(boardContent)
                    .boardImageUrl(s3Uploader.upload(multipartFile, "board"))
                    .boardDate(boardDate)
                    .user(loginUser)
                    .family(loginUser.getFamily())
                    .build();
            return boardRepository.save(board);
        }
    }

    //오늘의 기록 한개 가져오기
    @Transactional
    public BoardDto getOneBoard(final Long boardId){
        final Board board= boardRepository.findByBoardId(boardId)
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
    public void modifyBoard(final Long boardId, final MultipartFile multipartFile, final String boardContent){
        final User loginUser= authUtil.getUserByAuthentication();
        final Board board= boardRepository.findByBoardId(boardId)
                .orElseThrow(()-> new BoardNotExistException("board not exist", ErrorCode.BOARD_NOTEXIST));
        if(loginUser==board.getUser()){
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
    public void deleteBoard(final Long boardId){
        final User loginUser= authUtil.getUserByAuthentication();
        final String deletedImage= boardRepository.findByBoardId(boardId)
                .orElseThrow(()-> new BoardNotExistException("deletedImage not exist", ErrorCode.BOARD_NOTEXIST)).getBoardImageUrl();

        boardRepository.deleteByBoardIdAndUser(boardId,loginUser);
        if(deletedImage!=null) s3Uploader.deleteFile(deletedImage.substring(AWS_S3_BUCKET_URL.length()));
    }
}
