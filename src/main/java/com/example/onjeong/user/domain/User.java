package com.example.onjeong.user.domain;

import com.example.onjeong.error.ErrorCode;
import com.example.onjeong.family.domain.Family;
import com.example.onjeong.mail.domain.Mail;
import com.example.onjeong.profile.domain.Profile;
import com.example.onjeong.question.domain.Answer;
import com.example.onjeong.user.exception.UserNotExistException;
import lombok.*;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name="users")
public class User extends Common implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_nickname", nullable = false, unique = true)
    private String userNickname;

    @Column(name = "user_password", nullable = false)
    private String userPassword;

    @Column(name = "user_status", nullable = false)
    private String userStatus;

    @Column(name = "user_birth", nullable = false)
    private LocalDate userBirth;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "device_token")
    private String deviceToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="family_id")
    private Family family;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Profile profile;

    @OneToMany(mappedBy = "sendUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Mail> sendMailList;

    @OneToMany(mappedBy = "receiveUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Mail> receiveMailList;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Answer> answerList;

    public void updateUserName(String userName){ this.userName=userName; }

    public void setEncryptedPassword(String encryptedPassword) {
        this.userPassword = encryptedPassword;
    }

    public void updateUserStatus(String userStatus){
        this.userStatus=userStatus;
    }

    public void updateUserBirth(LocalDate userBirth){
        this.userBirth=userBirth;
    }

    public void updateDeviceToken(String deviceToken){
        this.deviceToken = deviceToken;
    }
}
