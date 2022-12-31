package com.example.onjeong.home.repository;

import com.example.onjeong.family.domain.Family;
import com.example.onjeong.home.domain.CoinHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CoinHistoryRepository extends JpaRepository<CoinHistory, Long> {

    @Query(nativeQuery = true,
            value="SELECT * FROM coin_history c WHERE c.family_id = :familyId" +
                    " ORDER BY c.coin_history_date DESC")
    List<CoinHistory> findByFamily(@Param("familyId") Long familyId);
}
