package com.example.onjeong.home.repository;

import com.example.onjeong.family.domain.Family;
import com.example.onjeong.home.domain.CoinHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CoinHistoryRepository extends JpaRepository<CoinHistory, Long> {
    List<CoinHistory> findByFamily(Family family);
    void deleteAllByFamily(Family family);
    List<CoinHistory> findAllByFamily(Family family);
}
