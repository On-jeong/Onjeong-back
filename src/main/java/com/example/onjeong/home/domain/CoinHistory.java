package com.example.onjeong.home.domain;

import com.example.onjeong.family.domain.Family;
import com.example.onjeong.user.domain.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class CoinHistory {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="coin_history_id")
    private Long coinHistoryId;

    @Enumerated(EnumType.STRING)
    private CoinHistoryType coinHistoryType; // family coin saved, used

    @Column(name="coin_amount")
    private Integer coinAmount;

    @Column(name="coin_history_date")
    private LocalDateTime coinHistoryDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id", nullable = false)
    private Family family;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
