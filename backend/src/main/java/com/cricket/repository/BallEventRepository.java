package com.cricket.repository;

import com.cricket.model.BallEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BallEventRepository extends JpaRepository<BallEvent, Long> {
    List<BallEvent> findByMatchIdOrderByTimestampDesc(Long matchId);
    List<BallEvent> findByMatchIdAndInningsNumberOrderByTimestampAsc(Long matchId, int inningsNumber);
    List<BallEvent> findTop20ByMatchIdOrderByTimestampDesc(Long matchId);
    void deleteByMatchId(Long matchId);
}
