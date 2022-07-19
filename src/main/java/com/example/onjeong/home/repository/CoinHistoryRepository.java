package com.example.onjeong.home.repository;

import com.example.onjeong.family.domain.Family;
import com.example.onjeong.home.domain.CoinHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoinHistoryRepository extends JpaRepository<CoinHistory, Long> {
    List<CoinHistory> findByFamily(Family family);
}
