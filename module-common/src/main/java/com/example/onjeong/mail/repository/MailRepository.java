package com.example.onjeong.mail.repository;

import com.example.onjeong.mail.domain.Mail;
import com.example.onjeong.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MailRepository extends JpaRepository<Mail, Long> {

    @Query(nativeQuery = true,
            value="SELECT * FROM mail m WHERE m.receiver_id = :id OR m.sender_id = :id")
    List<Mail> findByUser(@Param("id") Long id);

    @Query(nativeQuery = true,
            value="SELECT * FROM mail m WHERE m.receiver_id = :id AND m.check_receiver = false" +
            " ORDER BY m.mail_id DESC")
    Page<Mail> findByReceiver(Pageable pageable, @Param("id") Long id);

    @Query(nativeQuery = true,
            value="SELECT * FROM mail m WHERE m.sender_id = :id AND m.check_sender = false" +
            " ORDER BY m.mail_id DESC")
    Page<Mail> findBySender(Pageable pageable, @Param("id") Long id);

    @Query("SELECT m FROM Mail m WHERE m.receiverWantDelete = true AND m.senderWantDelete = true")
    List<Mail> findDeleteMail();

    void deleteAllBySendUser(User sendUser);
    void deleteAllByReceiveUser(User receiveUser);
    List<Mail> findAllBySendUser(User sendUser);
    List<Mail> findAllByReceiveUser(User receiveUser);
}
