package com.example.show_score.restcontroller.matchcontroller;

import com.example.show_score.model.MatchBean;
import com.example.show_score.service.MatchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/public/matches")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Public Match Information")
public class MatchPublicController {
    private final MatchService matchService;

    public MatchPublicController(MatchService matchService) {
        this.matchService = matchService;
    }

    // http://localhost:8080/api/public/matches
    @GetMapping
    public ArrayList<MatchBean> getMatches() {
        return new ArrayList<>(matchService.getAllMatches());
    }

    //http://localhost:8080/api/public/matches/{id}
    @GetMapping("/{id}")
    public ResponseEntity<MatchBean> getMatchById(@PathVariable Long id) {
        MatchBean matchBean = matchService.findById(id);
        if (matchBean != null) {
            return ResponseEntity.ok(matchBean);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
