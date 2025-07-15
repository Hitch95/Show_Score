package com.example.show_score.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "MATCH_BEAN")
public class MatchBean {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "team1 cannot be blank")
    @Column(name = "team1", nullable = false)
    private String team1;
    @NotBlank(message = "team2 cannot be blank")
    @Column(name = "team2", nullable = false)
    private String team2;
    @Column(name = "team1Score")
    private Integer team1Score = 0;
    @Column(name = "team2Score")
    private Integer team2Score = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "matchStatus")
    private MatchStatus matchStatus; // WAITING, IN_PROGRESS, FINISHED, CANCELED
    private String date;
    @Column(name = "startTime")
    private LocalDateTime startTime;
    @Column(name = "endTime")
    private LocalDateTime endTime;
    @Column(name = "sportType")
    private String sportType;
}
