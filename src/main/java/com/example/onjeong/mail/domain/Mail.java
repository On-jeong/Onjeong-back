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
    @GeneratedValue
    @Column(name="mail_id")
    private Long mailId;

    @Column(name="mail_content")
    private String mailContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sendUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiveUser;

    @Column(name="send_time")
    private LocalDateTime sendTime;

    @Column(name="check_read")
    boolean checkRead;

    @Column(name="sender_delete")
    boolean senderWantDelete;

    @Column(name="receiver_delete")
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
