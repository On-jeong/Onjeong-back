package com.example.onjeong.board.repository;

import com.example.onjeong.board.domain.Board;
import com.example.onjeong.family.domain.Family;
import com.example.onjeong.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board,Long>  {
    List<Board> findAllByBoardDateAndFamily(LocalDate boardDate, Family family);
    Optional<Board> findByBoardId(Long boardId);
    void deleteByBoardIdAndUser(Long boardId, User user);
    void deleteAllByUser(User user);
}
