package com.example.show_score.service;

import com.example.show_score.model.MatchBean;
import com.example.show_score.model.MatchStatus;
import com.example.show_score.model.UserBean;
import com.example.show_score.repository.MatchRepository;
import com.example.show_score.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchService {
    @Autowired
    private MatchRepository matchRepository;

    // ADMIN ONLY - Create a new match
    public MatchBean createMatch(MatchBean match) {
        if (match == null || match.getTeam1() == null || match.getTeam2() == null) {
            throw new RuntimeException("Match cannot be null or empty");
        }
        return matchRepository.save(match);
    }

    // READ operations
    // ALL USERS - Can see the scores of all the matchs
    public List<MatchBean> getAllMatches() {
        return matchRepository.findAll();
    }

    // ALL USERS - See a match by id
    public MatchBean findById(Long id) {
        return matchRepository.findById(id).orElse(null);
    }

    // ALL USERS - See a match by status (WAITING, IN_PROGRESS, FINISHED, CANCELED)
    public List<MatchBean> getMatchesByStatus(MatchStatus status) {
        return matchRepository.findByMatchStatus(status);
    }

    // ALL USERS - See matchs by sport type (e.g., Football, Basketball, etc.)
    public List<MatchBean> getMatchesBySport(String sportType) {
        return matchRepository.findBySportType(sportType);
    }

    // ADMIN ONLY - Update a complete match
    public MatchBean updateMatch(MatchBean match) {
        if (match == null || match.getId() == null) {
            throw new RuntimeException("Match or Match ID cannot be null");
        }
        return matchRepository.save(match);
    }

    // ORGANIZER + ADMIN - Update the score of the current match
    public MatchBean updateScore(Long matchId, int team1Score, int team2Score) {
        MatchBean match = findById(matchId);
        if (match == null) {
            throw new RuntimeException("Match not found");
        }
        match.setTeam1Score(team1Score);
        match.setTeam2Score(team2Score);
        return matchRepository.save(match);
    }

    // ORGANIZER + ADMIN - Update status of current match
    public MatchBean updateMatchStatus(Long matchId, MatchStatus status) {
        MatchBean match = findById(matchId);
        if (match == null) {
            throw new RuntimeException("Match not found");
        }
        match.setMatchStatus(status);
        return matchRepository.save(match);
    }

    // ADMIN ONLY - Delete a match
    public void deleteMatch(Long id) {
        matchRepository.deleteById(id);
    }
}
