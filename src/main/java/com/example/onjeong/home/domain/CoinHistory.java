package com.example.onjeong.home.domain;

import com.example.onjeong.family.domain.Family;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class CoinHistory {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="coin_history_id")
    private Long coinHistory_id;

    @Enumerated(EnumType.STRING)
    private CoinHistoryType type; // family coin saved, used

    @Column(name="amount")
    private Integer amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id", nullable = false)
    private Family family;
}
