package com.example.onjeong.home.repository;

import com.example.onjeong.home.domain.Flower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FlowerRepository extends JpaRepository<Flower, Long> {

    @Query(nativeQuery = true,
            value = "SELECT * FROM flower f WHERE f.family_id = :id " +
                    "AND f.flower_bloom = false limit 1")
    Flower findBlooming(@Param("id") Long id);

    @Query(nativeQuery = true,
            value = "SELECT * FROM flower f WHERE f.family_id = :id " +
                    "AND f.flower_bloom = true")
    List<Flower> findFullBloom(@Param("id") Long id);
}
