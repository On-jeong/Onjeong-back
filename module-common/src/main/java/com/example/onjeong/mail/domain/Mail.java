package com.example.onjeong.mail.domain;

import com.example.onjeong.user.domain.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Mail {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="mail_id", nullable = false, unique = true)
    private Long mailId;

    @Column(name="mail_content", nullable = false)
    private String mailContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sendUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiveUser;

    @Column(name="send_time", nullable = false)
    private LocalDateTime sendTime;

    @Column(name="check_read", nullable = false)
    boolean checkRead;

    @Column(name="check_sender", nullable = false)
    boolean senderWantDelete;

    @Column(name="check_receiver", nullable = false)
    boolean receiverWantDelete;

    public void Read(){
        this.checkRead = true;
    }

    public void deleteSend(){
        this.senderWantDelete = true;
    }

    public void deleteReceive(){
        this.receiverWantDelete = true;
    }



}
