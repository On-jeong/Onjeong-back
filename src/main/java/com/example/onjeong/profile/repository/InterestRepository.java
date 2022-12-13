package com.example.onjeong.profile.repository;

import com.example.onjeong.profile.domain.Hate;
import com.example.onjeong.profile.domain.Interest;
import com.example.onjeong.profile.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterestRepository extends JpaRepository<Interest,Long> {
    void deleteByInterestIdAndProfile(Long interestId, Profile profile);
    void deleteAllByProfile(Profile profile);
}
