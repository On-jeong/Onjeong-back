package com.example.onjeong.profile.repository;

import com.example.onjeong.profile.domain.Interest;
import com.example.onjeong.profile.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestRepository extends JpaRepository<Interest,Long> {
    void deleteByInterestIdAndProfile(Long interestId, Profile profile);
    void deleteAllByProfile(Profile profile);
}
