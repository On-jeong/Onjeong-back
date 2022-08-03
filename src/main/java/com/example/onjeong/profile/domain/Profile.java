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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="family_id")
    private Family family;

    @ManyToOne(fetch = FetchType.LAZY)
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


    public void setMessage(String message){
        this.message=message;
    }

    public void setProfileImageUrl(String profileImageUrl){
        this.profileImageUrl=profileImageUrl;
    }

    public void setCheckProfileImage(boolean checkProfileImage){
        this.checkProfileImage=checkProfileImage;
    }

    public List<String> favoriteToString(List<Favorite> favorites){
        List<String> result= new ArrayList<>();
        for(Favorite f:favorites){
            result.add(f.getFavoriteContent());
        }
        return result;
    }

    public List<String> hateToString(List<Hate> hates){
        List<String> result= new ArrayList<>();
        for(Hate h:hates){
            result.add(h.getHateContent());
        }
        return result;
    }

    public List<String> expressionToString(List<Expression> introductions){
        List<String> result= new ArrayList<>();
        for(Expression i:introductions){
            result.add(i.getExpressionContent());
        }
        return result;
    }

    public List<String> interestToString(List<Interest> interests){
        List<String> result= new ArrayList<>();
        for(Interest i:interests){
            result.add(i.getInterestContent());
        }
        return result;
    }
}
