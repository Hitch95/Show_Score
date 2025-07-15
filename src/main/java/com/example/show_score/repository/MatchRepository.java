package com.example.show_score.repository;

import com.example.show_score.model.MatchBean;
import com.example.show_score.model.MatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<MatchBean, Long> {
    // Additional query methods can be defined here if needed
    List<MatchBean> findByMatchStatus(MatchStatus matchStatus);
    List<MatchBean> findBySportType(String sportType);
}
