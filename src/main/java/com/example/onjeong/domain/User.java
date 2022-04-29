package com.example.onjeong.domain;

import com.example.onjeong.dto.UserJoinDto;
import com.example.onjeong.dto.UserJoinedDto;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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
    private Date userBirth;

    @Enumerated(EnumType.STRING)
    private UserRole role;


    @ManyToOne
    @JoinColumn(name="family_id")
    private Family family;

/*
    public User(UserJoinDto userJoinDto){
        Family family=new Family();
        this.userName=userJoinDto.getUserName();
        this.userNickname=userJoinDto.getUserNickname();
        this.userPassword=userJoinDto.getUserPassword();
        this.userStatus=userJoinDto.getUserStatus();
        this.userBirth=userJoinDto.getUserBirth();
        this.role=UserRole.USER;
    }

    public User(UserJoinedDto userJoinedDto){
        this.userName=userJoinedDto.getUserName();
        this.userNickname=userJoinedDto.getUserNickname();
        this.userPassword=userJoinedDto.getUserPassword();
        this.userStatus=userJoinedDto.getUserStatus();
        this.userBirth=userJoinedDto.getUserBirth();
        this.role=UserRole.USER;
    }

    public void encryptPassword(PasswordEncoder passwordEncoder) {
        this.userPassword = passwordEncoder.encode(userPassword);
    }

 */

}
