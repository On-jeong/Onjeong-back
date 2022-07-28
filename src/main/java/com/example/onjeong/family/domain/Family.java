package com.example.onjeong.family.domain;

import com.example.onjeong.home.domain.CoinHistory;
import com.example.onjeong.home.domain.Flower;
import com.example.onjeong.question.domain.Question;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
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

    @OneToMany(mappedBy = "family", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Question> questionList;

    @OneToMany(mappedBy = "family", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Flower> flowerList;

    @OneToMany(mappedBy = "family", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CoinHistory> coinHistoryList;

    public Family(){
        this.familyCoin=0;
    }

    public void updateCoin(Integer amount){
        this.familyCoin += amount;
    }
}
