package com.example.onjeong.user.domain;

import com.example.onjeong.family.domain.Family;
import lombok.*;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

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

    //@Column(name = "user_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="family_id")
    private Family family;

    public void updateUserName(String userName){ this.userName=userName; }

    public void updateUserNickname(String userNickname){
        this.userNickname=userNickname;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.userPassword = encryptedPassword;
    }

    public void updateUserStatus(String userStatus){
        this.userStatus=userStatus;
    }

    public void updateUserBirth(LocalDate userBirth){
        this.userBirth=userBirth;
    }

    public void updateRole(UserRole role){
        this.role=role;
    }

    public void updateFamily(Family family){
        this.family=family;
    }
}
