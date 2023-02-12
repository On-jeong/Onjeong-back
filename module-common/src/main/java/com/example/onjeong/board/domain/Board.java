package com.example.onjeong.board.domain;

import com.example.onjeong.family.domain.Family;
import com.example.onjeong.user.domain.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="boards")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id", nullable = false, unique = true)
    private Long boardId;

    @Column(name = "board_content", nullable = false)
    private String boardContent;

    @Column(name = "board_image_url")
    private String boardImageUrl;

    @Column(name = "board_date", nullable = false)
    private LocalDate boardDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="family_id")
    private Family family;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    public void updateBoardContent(String boardContent){ this.boardContent=boardContent; }

    public void updateBoardDate(LocalDate boardDate){ this.boardDate=boardDate; }

    public void updateBoardImageUrl(String boardImageUrl){ this.boardImageUrl=boardImageUrl; }
}
