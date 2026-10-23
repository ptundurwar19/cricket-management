package com.cricket.repository;

import com.cricket.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findByCountry(String country);
    List<Player> findTop10ByOrderByTotalRunsDesc();
    List<Player> findTop10ByOrderByTotalWicketsDesc();
}
