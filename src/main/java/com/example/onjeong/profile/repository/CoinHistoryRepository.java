package com.example.onjeong.profile.repository;

import com.example.onjeong.family.domain.Family;
import com.example.onjeong.profile.domain.CoinHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoinHistoryRepository extends JpaRepository<CoinHistory, Long> {
    List<CoinHistory> findByFamily(Family family);
}
