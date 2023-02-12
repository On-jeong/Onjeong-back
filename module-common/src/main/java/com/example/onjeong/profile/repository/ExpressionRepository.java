package com.example.onjeong.profile.repository;

import com.example.onjeong.profile.domain.Expression;
import com.example.onjeong.profile.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpressionRepository extends JpaRepository<Expression,Long> {
    void deleteByExpressionIdAndProfile(Long expressionId, Profile profile);
    void deleteAllByProfile(Profile profile);
}
