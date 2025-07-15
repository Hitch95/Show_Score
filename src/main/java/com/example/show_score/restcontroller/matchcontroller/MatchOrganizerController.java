package com.example.show_score.restcontroller.matchcontroller;

import com.example.show_score.model.MatchBean;
import com.example.show_score.model.MatchStatus;
import com.example.show_score.service.MatchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organizer/matches")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Organizer Match Management")
@PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')")
public class MatchOrganizerController {
    private final MatchService matchService;

    public MatchOrganizerController(MatchService matchService) {
        this.matchService = matchService;
    }

    // PUT
    // http:localhost:8080/api/organizer/matches/updateScore
    @PutMapping("/updateScore")
    public ResponseEntity<MatchBean> updateScore(
            @RequestParam Long id,
            @RequestParam Integer team1Score,
            @RequestParam Integer team2Score
    ) {
        try {
            MatchBean updatedMatch = matchService.updateScore(id, team1Score, team2Score);
            return ResponseEntity.ok(updatedMatch);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // PUT
    // http:localhost8080/api/organizer/matches/updateStatus/{id}
    @PutMapping("/updateStatus/{id}")
    public ResponseEntity<MatchBean> updateMatchStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {
        try {
            MatchBean updatedMatch = matchService.updateMatchStatus(id, MatchStatus.valueOf(status));
            return ResponseEntity.ok(updatedMatch);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}