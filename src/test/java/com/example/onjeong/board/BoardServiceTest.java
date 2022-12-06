package com.example.onjeong.board;

import com.example.onjeong.S3.S3Uploader;
import com.example.onjeong.board.domain.Board;
import com.example.onjeong.board.dto.BoardDto;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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





    private MockMultipartFile getMockMultipartFile() throws IOException {
        final String fileName = "fileName";
        final String contentType = "contentType";
        final String filePath = "src/test/resources/rabbit photo.png";
        FileInputStream fileInputStream = new FileInputStream(new File(filePath));
        return new MockMultipartFile(fileName, fileName + "." + contentType, contentType, fileInputStream);
    }
}
