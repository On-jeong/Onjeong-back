package com.example.onjeong.home.domain;

import com.example.onjeong.family.domain.Family;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Flower {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="flower_id", nullable = false, unique = true)
    private Long flowerId;

    @Enumerated(EnumType.STRING)
    private FlowerKind flowerKind;

    @Enumerated(EnumType.STRING)
    private FlowerColor flowerColor;

    @Column(name="flower_bloom")
    private Boolean flowerBloom;

    @Column(name="flower_level", nullable = false)
    private Integer flowerLevel;

    @Column(name="flower_bloom_date")
    private LocalDate flowerBloomDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id", nullable = false)
    private Family family;

    public void levelUp(){
        this.flowerLevel++;

        if(this.flowerLevel == 10){
            this.flowerBloom = true;
            this.flowerBloomDate = LocalDate.now();
        }
    }
}
