package com.example.onjeong.user.repository;

import com.example.onjeong.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserNickname(String userNickname);
    boolean existsByUserNickname(String userNickname);
    Optional<User> findByRefreshToken(String refreshToken);

    @Modifying
    @Query(nativeQuery = true,
            value="DELETE FROM users u WHERE u.user_id = :userId")
    void deleteUser(@Param("userId") Long userId);
}
