package com.example.onjeong.profile.domain;

import com.example.onjeong.family.domain.Family;
import com.example.onjeong.user.domain.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="profiles")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id", nullable = false, unique = true)
    private Long profileId;

    @Column(name = "message")
    private String message;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "check_profile_image")
    private boolean checkProfileImage;

    @Column(name = "check_profile_upload")
    private boolean checkProfileUpload;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="family_id")
    private Family family;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private final List<Favorite> favorites = new ArrayList<>();

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private final List<Hate> hates = new ArrayList<>();

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL)     //한단어로 표현
    private final List<Expression> expressions = new ArrayList<>();

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL)     //관심사
    private final List<Interest> interests = new ArrayList<>();


    public void updateMessage(String message){
        this.message=message;
    }

    public void updateProfileImageUrl(String profileImageUrl){
        this.profileImageUrl=profileImageUrl;
    }

    public void updateCheckProfileImage(boolean checkProfileImage){
        this.checkProfileImage=checkProfileImage;
    }

    public void updateCheckProfileUpload(boolean checkProfileUpload){
        this.checkProfileUpload=checkProfileUpload;
    }
}
