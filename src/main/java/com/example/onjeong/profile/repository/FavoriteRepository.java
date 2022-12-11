package com.example.onjeong.profile.repository;

import com.example.onjeong.profile.domain.Expression;
import com.example.onjeong.profile.domain.Favorite;
import com.example.onjeong.profile.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite,Long> {
    void deleteByFavoriteIdAndProfile(Long favoriteId, Profile profile);
    void deleteAllByProfile(Profile profile);
}
