package com.example.show_score.restcontroller.matchcontroller;

import com.example.show_score.model.MatchBean;
import com.example.show_score.service.MatchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/admin/matches")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Admin Match Management")
@PreAuthorize("hasRole('ADMIN')")
public class MatchAdminController {
    private final MatchService matchService;

    public MatchAdminController(MatchService matchService) {
        this.matchService = matchService;
    }

    // GET
    // http:localhost:8080/api/admin/matches
    @GetMapping
    public ArrayList<MatchBean> getAllMatches() {
        return new ArrayList<>(matchService.getAllMatches());
    }

    // GET
    // http:localhost:8080/api/admin/matches/{id}
    @GetMapping("/{id}")
    public ResponseEntity<MatchBean> getMatchById(@PathVariable Long id) {
        MatchBean matchBean = matchService.findById(id);
        if (matchBean != null) {
            return ResponseEntity.ok(matchBean);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // POST
    // http:localhost:8080/api/admin/matches
    @PostMapping
    public ResponseEntity<MatchBean> createMatch(@RequestBody MatchBean matchBean) {
        try {
            MatchBean createdMatch = matchService.createMatch(matchBean);
            return new ResponseEntity<>(createdMatch, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // PUT
    // http:localhost:8080/api/admin/matches/{id}
    @PutMapping("/{id}")
    public ResponseEntity<MatchBean> updateMatch(
            @PathVariable Long id,
            @RequestBody MatchBean matchBean
    ) {
        try {
            matchBean.setId(id);
            MatchBean updatedMatch = matchService.updateMatch(matchBean);
            return ResponseEntity.ok(updatedMatch);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // PUT
    // http:localhost:8080/api/admin/matches/updateScore
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

    // DELETE
    // http:localhost:8080/api/admin/matches/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable Long id) {
        try {
            matchService.deleteMatch(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}