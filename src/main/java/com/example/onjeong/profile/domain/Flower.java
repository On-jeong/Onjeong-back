package com.example.onjeong.profile.domain;

import com.example.onjeong.family.domain.Family;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Flower {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="flower_id")
    private Long flowerId;

    @Enumerated(EnumType.STRING)
    private FlowerKind flowerKind;

    @Column(name="flower_bloom")
    private Boolean flowerBloom;

    @Column(name="flower_level")
    private Integer flowerLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id", nullable = false)
    private Family family;


    public void levelUp(){
        this.flowerLevel++;

        if(this.flowerLevel == 20){
            this.flowerBloom = true;
        }
    }
}
