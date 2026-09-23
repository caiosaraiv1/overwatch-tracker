package com.caio.overwatch_tracker.performance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {

    List<Performance> findByMatchId(Long matchId);
    List<Performance> findByHeroId(Long heroId);
}
