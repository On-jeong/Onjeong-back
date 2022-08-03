package com.example.onjeong.mail.repository;

import com.example.onjeong.mail.domain.Mail;
import com.example.onjeong.question.domain.Question;
import com.example.onjeong.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MailRepository extends JpaRepository<Mail, Long> {

    @Query(nativeQuery = true,
            value="SELECT * FROM mail m WHERE m.receiver_id = :id OR m.sender_id = :id")
    List<Mail> findByUser(@Param("id") Long id);

    @Query(nativeQuery = true,
            value="SELECT * FROM mail m WHERE m.receiver_id = :id AND m.receiver_delete = false" +
            " ORDER BY m.send_time DESC LIMIT 30")
    List<Mail> findByReceiver(@Param("id") Long id);

    @Query(nativeQuery = true,
            value="SELECT * FROM mail m WHERE m.sender_id = :id AND m.sender_delete = false" +
            " ORDER BY m.send_time DESC LIMIT 30")
    List<Mail> findBySender(@Param("id") Long id);

    @Query("SELECT m FROM Mail m WHERE m.receiverWantDelete = true AND m.senderWantDelete = true")
    List<Mail> findDeleteMail();
}
