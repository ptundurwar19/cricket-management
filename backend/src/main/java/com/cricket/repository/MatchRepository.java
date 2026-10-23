package com.cricket.repository;

import com.cricket.model.CricketMatch;
import com.cricket.model.MatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<CricketMatch, Long> {
    List<CricketMatch> findByStatus(MatchStatus status);
    List<CricketMatch> findByStatusIn(List<MatchStatus> statuses);
}
