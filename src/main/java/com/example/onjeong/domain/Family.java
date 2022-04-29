package com.example.onjeong.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Entity
@Table(name="families")
public class Family {
    @Id
    @GeneratedValue
    @Column(name = "family_id", nullable = false, unique = true)
    private Long familyId;

    @Column(name = "family_coin", nullable = false)
    private Integer familyCoin;

    public Family(){
        this.familyCoin=0;
    }
}
