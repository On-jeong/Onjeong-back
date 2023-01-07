package com.example.onjeong.family.domain;


import com.example.onjeong.anniversary.domain.Anniversary;
import com.example.onjeong.user.domain.User;
import java.util.ArrayList;
import com.example.onjeong.coin.domain.CoinHistory;
import com.example.onjeong.home.domain.Flower;
import com.example.onjeong.question.domain.Question;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
@Builder
@Entity
@Table(name="families")
public class Family {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "family_id", nullable = false, unique = true)
    private Long familyId;

    @Column(name = "family_coin", nullable = false)
    private Integer familyCoin;

    @OneToMany(mappedBy = "family", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private final List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "family", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Question> questionList;

    @OneToMany(mappedBy = "family", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Flower> flowerList;

    @OneToMany(mappedBy = "family", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<CoinHistory> coinHistoryList;

    @OneToMany(mappedBy = "family", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private final List<Anniversary> anniversaryList = new ArrayList<>();


    public void updateCoin(Integer amount){
        this.familyCoin += amount;
    }
}
