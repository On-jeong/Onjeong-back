package com.example.onjeong.board;

import com.example.onjeong.S3.S3Uploader;
import com.example.onjeong.board.domain.Board;
import com.example.onjeong.board.dto.BoardDto;
import com.example.onjeong.board.exception.BoardNotExistException;
import com.example.onjeong.board.exception.BoardWriterNotSameException;
import com.example.onjeong.board.repository.BoardRepository;
import com.example.onjeong.board.service.BoardService;
import com.example.onjeong.family.domain.Family;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.util.AuthUtil;
import com.example.onjeong.util.BoardUtils;
import com.example.onjeong.util.FamilyUtils;
import com.example.onjeong.util.UserUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

    @InjectMocks
    private BoardService boardService;

    @Mock
    private AuthUtil authUtil;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private S3Uploader s3Uploader;


    @Test
    void 오늘의_기록_모두_가져오기(){
        //given
        final LocalDate boardDate= LocalDate.of(2020,12,03);
        final List<Board> boards= new ArrayList<>();
        final Family family= FamilyUtils.getRandomFamily();
        final User user= UserUtils.getRandomUser(family);
        for(int i=0;i<3;i++){
            final Board board= BoardUtils.getRandomBoard(family, user);
            boards.add(board);
        }

        doReturn(user).when(authUtil).getUserByAuthentication();
        doReturn(boards).when(boardRepository).findAllByBoardDateAndFamily(boardDate, user.getFamily());


        //when
        List<BoardDto> result= boardService.getAllBoard(boardDate);


        //then
        assertThat(result.size()).isEqualTo(3);

    }

    @Nested
    class 오늘의_기록_작성하기{
        @Test
        void 사진이없을경우(){
            //given
            final LocalDate boardDate= LocalDate.of(2020,12,03);
            final MultipartFile multipartFile= null;
            final String boardContent= "nice day";
            final Family family= FamilyUtils.getRandomFamily();
            final User user= UserUtils.getRandomUser(family);

            doReturn(user).when(authUtil).getUserByAuthentication();


            //when
            boardService.registerBoard(boardDate, multipartFile, boardContent);


            //then
            verify(boardRepository,times(1)).save(any(Board.class));
            verify(s3Uploader,times(0)).upload(any(MultipartFile.class),any(String.class));
        }


        @Test
        void 사진이있을경우() throws IOException {
            //given
            final LocalDate boardDate= LocalDate.of(2020,12,03);
            final MockMultipartFile multipartFile = getMockMultipartFile();
            final String boardContent= "nice day";
            final Family family= FamilyUtils.getRandomFamily();
            final User user= UserUtils.getRandomUser(family);

            doReturn(user).when(authUtil).getUserByAuthentication();


            //when
            boardService.registerBoard(boardDate, multipartFile, boardContent);


            //then
            verify(boardRepository,times(1)).save(any(Board.class));
            verify(s3Uploader,times(1)).upload(any(MultipartFile.class),any(String.class));
        }
    }

    @Nested
    class 오늘의_기록_한개_가져오기{
        @Test
        void 성공(){
            //given
            final Long boardId= 1L;
            final Family family= FamilyUtils.getRandomFamily();
            final User user= UserUtils.getRandomUser(family);
            final Board board= BoardUtils.getRandomBoard(family, user);

            doReturn(Optional.of(board)).when(boardRepository).findByBoardId(boardId);

            //when
            final BoardDto boardDto= boardService.getOneBoard(boardId);

            //then
            assertThat(boardDto.getBoardContent()).isEqualTo(board.getBoardContent());
            assertThat(boardDto.getBoardImageUrl()).isEqualTo(board.getBoardImageUrl());
        }

        @Test
        void 실패(){
            //given
            final Long boardId= 1L;

            doReturn(Optional.empty()).when(boardRepository).findByBoardId(boardId);


            //when
            Throwable exception = assertThrows(BoardNotExistException.class, () -> {
                boardService.getOneBoard(boardId);
            });


            //then
            assertThat(exception.getMessage()).isEqualTo("board not exist");
        }
    }

    @Nested
    class 오늘의_기록_수정하기{
        @Test
        void 사진추가_x_이전의사진파일이_x(){
            //given
            final Long boardIdToModify= 1L;
            final MultipartFile fileToModify= null;
            final String boardContentToModify= "nice day";
            final Family family= FamilyUtils.getRandomFamily();
            final User user= UserUtils.getRandomUser(family);
            final Board registeredBoard= BoardUtils.getBoard(1L, "random content", null,
                    LocalDate.of(2022,12,02), family, user);

            doReturn(user).when(authUtil).getUserByAuthentication();
            doReturn(Optional.of(registeredBoard)).when(boardRepository).findByBoardId(boardIdToModify);


            //when
            boardService.modifyBoard(boardIdToModify, fileToModify, boardContentToModify);


            //then
            verify(s3Uploader,times(0)).deleteFile(any(String.class));
            verify(s3Uploader,times(0)).upload(any(MultipartFile.class),any(String.class));
        }

        @Test
        void 사진추가_x_이전의사진파일이_o(){
            //given
            final Long boardIdToModify= 1L;
            final MultipartFile fileToModify= null;
            final String boardContentToModify= "nice day";
            final Family family= FamilyUtils.getRandomFamily();
            final User user= UserUtils.getRandomUser(family);
            final Board registeredBoard= BoardUtils.getBoard(1L, "random content", "url",
                    LocalDate.of(2022,12,02), family, user);

            ReflectionTestUtils.setField(boardService, "AWS_S3_BUCKET_URL", "");

            doReturn(user).when(authUtil).getUserByAuthentication();
            doReturn(Optional.of(registeredBoard)).when(boardRepository).findByBoardId(boardIdToModify);

            //when
            boardService.modifyBoard(boardIdToModify, fileToModify, boardContentToModify);


            //then
            verify(s3Uploader,times(1)).deleteFile(any(String.class));
            verify(s3Uploader,times(0)).upload(any(MultipartFile.class),any(String.class));
        }

        @Test
        void 사진추가_o_이전의사진파일이_x() throws IOException{
            //given
            final Long boardIdToModify= 1L;
            final MockMultipartFile fileToModify= getMockMultipartFile();
            final String boardContentToModify= "nice day";
            final Family family= FamilyUtils.getRandomFamily();
            final User user= UserUtils.getRandomUser(family);
            final Board registeredBoard= BoardUtils.getBoard(1L, "random content", null,
                    LocalDate.of(2022,12,02), family, user);

            ReflectionTestUtils.setField(boardService, "AWS_S3_BUCKET_URL", "");

            doReturn(user).when(authUtil).getUserByAuthentication();
            doReturn(Optional.of(registeredBoard)).when(boardRepository).findByBoardId(boardIdToModify);


            //when
            boardService.modifyBoard(boardIdToModify, fileToModify, boardContentToModify);


            //then
            verify(s3Uploader,times(0)).deleteFile(any(String.class));
            verify(s3Uploader,times(1)).upload(any(MultipartFile.class),any(String.class));
        }

        @Test
        void 사진추가_o_이전의사진파일이_o() throws IOException{
            //given
            final Long boardIdToModify= 1L;
            final MockMultipartFile fileToModify= getMockMultipartFile();
            final String boardContentToModify= "nice day";
            final Family family= FamilyUtils.getRandomFamily();
            final User user= UserUtils.getRandomUser(family);
            final Board registeredBoard= BoardUtils.getBoard(1L, "random content", "url",
                    LocalDate.of(2022,12,02), family, user);

            ReflectionTestUtils.setField(boardService, "AWS_S3_BUCKET_URL", "");

            doReturn(user).when(authUtil).getUserByAuthentication();
            doReturn(Optional.of(registeredBoard)).when(boardRepository).findByBoardId(boardIdToModify);


            //when
            boardService.modifyBoard(boardIdToModify, fileToModify, boardContentToModify);


            //then
            verify(s3Uploader,times(1)).deleteFile(any(String.class));
            verify(s3Uploader,times(1)).upload(any(MultipartFile.class),any(String.class));
        }

        @Test
        void 로그인한사용자와_글작성자가_다를경우_예외처리(){
            //given
            final Family family= FamilyUtils.getRandomFamily();
            final User user= UserUtils.getRandomUser(family);
            final User otherUser= UserUtils.getRandomUser(family);
            final Board board= BoardUtils.getRandomBoard(family,otherUser);

            doReturn(user).when(authUtil).getUserByAuthentication();
            doReturn(Optional.of(board)).when(boardRepository).findByBoardId(any(Long.class));


            //when
            Throwable exception= assertThrows(BoardWriterNotSameException.class,()->{
                boardService.modifyBoard(1L, getMockMultipartFile(), "board content");
            });


            //then
            assertThat(exception.getMessage()).isEqualTo("board writer not same");
        }
    }


    @Nested
    class 오늘의_기록_삭제하기{
        @Test
        void 삭제할사진파일이_없을경우(){
            //given
            final Long boardId= 1L;
            final Family family= FamilyUtils.getRandomFamily();
            final User user= UserUtils.getRandomUser(family);
            final Board board= BoardUtils.getBoard(1L, "random content", null,
                    LocalDate.of(2022,12,02), family, user);

            ReflectionTestUtils.setField(boardService, "AWS_S3_BUCKET_URL", "");

            doReturn(user).when(authUtil).getUserByAuthentication();
            doReturn(Optional.of(board)).when(boardRepository).findByBoardId(boardId);


            //when
            boardService.deleteBoard(boardId);


            //then
            verify(s3Uploader,times(0)).deleteFile(any(String.class));
        }

        @Test
        void 삭제할사진파일이_있을경우(){
            //given
            final Long boardId= 1L;
            final Family family= FamilyUtils.getRandomFamily();
            final User user= UserUtils.getRandomUser(family);
            final Board board= BoardUtils.getBoard(1L, "random content", "url",
                    LocalDate.of(2022,12,02), family, user);

            ReflectionTestUtils.setField(boardService, "AWS_S3_BUCKET_URL", "");

            doReturn(user).when(authUtil).getUserByAuthentication();
            doReturn(Optional.of(board)).when(boardRepository).findByBoardId(boardId);


            //when
            boardService.deleteBoard(boardId);


            //then
            verify(s3Uploader,times(1)).deleteFile(any(String.class));
        }
    }



    private MockMultipartFile getMockMultipartFile() throws IOException {
        return new MockMultipartFile("images", "", "image/png", FileInputStream.nullInputStream());
    }
}
