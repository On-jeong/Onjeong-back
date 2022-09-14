package com.example.onjeong.anniversary.repository;

import com.example.onjeong.anniversary.domain.Anniversary;
import com.example.onjeong.family.domain.Family;
import com.example.onjeong.mail.domain.Mail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnniversaryRepository extends JpaRepository<Anniversary,Long> {
    Optional<List<Anniversary>> findAllByAnniversaryDateBetweenAndFamily(LocalDate start, LocalDate end, Family family);
    Optional<List<Anniversary>> findAllByAnniversaryDateAndFamily(LocalDate anniversaryDate, Family family);
    Optional<Anniversary> findByAnniversaryIdAndFamily(Long anniversaryId, Family family);
    String deleteByAnniversaryIdAndFamily(Long anniversaryId, Family family);
    void deleteByFamily(Family family);

    Optional<List<Anniversary>> findAllByAnniversaryDate(LocalDate anniversaryDate);

    @Query(nativeQuery = true,
            value="SELECT * FROM anniversaries a WHERE a.anniversary_date = :anniversaryDate AND a.family_id = :familyId" +
                    " ORDER BY a.anniversary_id ASC LIMIT 3")
    Optional<List<Anniversary>> findByAnniversaryDate(@Param("anniversaryDate") LocalDate anniversaryDate, @Param("familyId") Long familyId);

}
