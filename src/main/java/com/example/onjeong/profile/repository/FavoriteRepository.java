package com.example.onjeong.profile.repository;

import com.example.onjeong.profile.domain.Favorite;
import com.example.onjeong.profile.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite,Long> {
    String deleteByFavoriteIdAndProfile(Long favoriteId, Profile profile);
}
