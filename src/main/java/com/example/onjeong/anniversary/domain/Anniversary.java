package com.example.onjeong.anniversary.domain;

import com.example.onjeong.family.domain.Family;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;


@Getter
@AllArgsConstructor
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="anniversaries")
public class Anniversary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "anniversary_id", nullable = false, unique = true)
    private Long anniversaryId;

    @Column(name = "anniversary_content", nullable = false)     //글자수 제한 필요
    private String anniversaryContent;

    @Column(name = "anniversary_date", nullable = false)
    private LocalDate anniversaryDate;

    @ManyToOne
    @JoinColumn(name="family_id")
    private Family family;


    public void updateAnniversaryContent(String anniversaryContent){ this.anniversaryContent=anniversaryContent; }

    public void updateAnniversaryDate(LocalDate anniversaryDate){ this.anniversaryDate=anniversaryDate; }
}
