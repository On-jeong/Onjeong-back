package com.example.onjeong.user.repository;

import com.example.onjeong.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserNickname(String userNickname);
    User findByUserNicknameAndUserPassword(String userNickname, String userPassword);
    boolean existsByUserNickname(String userNickname);
}
