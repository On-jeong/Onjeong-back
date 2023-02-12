package com.example.onjeong.profile.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="hates")
public class Hate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hate_id", nullable = false, unique = true)
    private Long hateId;

    @Column(name = "hate_content")
    private String hateContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="profile_id")
    private Profile profile;
}
