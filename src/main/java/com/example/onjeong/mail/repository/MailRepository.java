package com.example.onjeong.mail.repository;

import com.example.onjeong.mail.domain.Mail;
import com.example.onjeong.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MailRepository extends JpaRepository<Mail, Long> {

    @Query("SELECT m FROM Mail m WHERE m.receiveUser = :user AND m.receiverWantDelete = false")
    List<Mail> findByReceiver(@Param("user") User user);

    @Query("SELECT m FROM Mail m WHERE m.sendUser = :user AND m.senderWantDelete = false")
    List<Mail> findBySender(@Param("user") User user);

    @Query("SELECT m FROM Mail m WHERE m.receiverWantDelete = true AND m.senderWantDelete = true")
    List<Mail> findDeleteMail();
}