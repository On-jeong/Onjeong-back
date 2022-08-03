package com.example.onjeong.profile.repository;

import com.example.onjeong.profile.domain.Hate;
import com.example.onjeong.profile.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HateRepository extends JpaRepository<Hate,Long> {
    String deleteByHateIdAndProfile(Long hateId, Profile profile);
}
