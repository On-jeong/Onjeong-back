package com.example.onjeong.profile.repository;

import com.example.onjeong.profile.domain.Expression;
import com.example.onjeong.profile.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpressionRepository extends JpaRepository<Expression,Long> {
    void deleteByExpressionIdAndProfile(Long expressionId, Profile profile);
    void deleteAllByProfile(Profile profile);
}
