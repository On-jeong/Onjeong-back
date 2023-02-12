package com.example.onjeong.profile.repository;

import com.example.onjeong.profile.domain.Profile;
import com.example.onjeong.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile,Long> {
    Optional<Profile> findByUser(User user);
    void deleteByUser(User user);
}
