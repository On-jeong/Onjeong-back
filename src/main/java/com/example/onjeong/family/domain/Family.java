package com.example.onjeong.family.domain;

import com.example.onjeong.quesiton.domain.Question;
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

    public Family(){
        this.familyCoin=0;
    }
}
