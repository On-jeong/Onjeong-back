package com.example.onjeong.profile.domain;

import lombok.*;

import javax.persistence.*;


@Getter
@AllArgsConstructor
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="expressions")
public class Expression {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expression_id", nullable = false, unique = true)
    private Long expressionId;

    @Column(name = "expression_content")
    private String expressionContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="profile_id")
    private Profile profile;
}
