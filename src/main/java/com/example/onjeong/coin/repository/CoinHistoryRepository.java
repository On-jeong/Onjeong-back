package com.example.onjeong.coin.repository;

import com.example.onjeong.coin.domain.CoinHistory;
import com.example.onjeong.coin.domain.CoinHistoryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CoinHistoryRepository extends JpaRepository<CoinHistory, Long> {

    @Query(nativeQuery = true,
            value="SELECT * FROM coin_history c WHERE c.family_id = :familyId" +
                    " ORDER BY c.coin_history_id DESC")
    Page<CoinHistory> findByFamily(Pageable pageable, @Param("familyId") Long familyId);

    @Query(nativeQuery = true,
            value="SELECT * FROM coin_history c " +
                    " WHERE date_format(c.coin_history_date, '%Y-%m-%d') = DATE_FORMAT(now(),'%Y-%m-%d') " +
                    " AND c.coin_history_type = 'RAND' AND c.user_id = :userId")
    List<CoinHistory> findByDateAndType(@Param("userId")Long userId);
}
