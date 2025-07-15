import { Component, OnInit } from '@angular/core';
import { Api } from '../services/api';
import { Match } from '../models/match';
import { CommonModule } from '@angular/common';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-show-score',
  templateUrl: './show-score.html',
  styleUrls: ['./show-score.css'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class ShowScore implements OnInit {
  matches: Match[] = [];
  errorMessage: string = '';
  isLoading = false;
  private currentUserRole: string | null = null;

  constructor(private api: Api) {}

  ngOnInit(): void {
    this.loadMatches();
    this.getCurrentUserRole();
  }

  loadMatches(): void {
    this.isLoading = true;
    this.api.getMatches().subscribe({
      next: (data) => {
      this.matches = data;
      this.isLoading = false;
      },
      error: (error) => {
        this.errorMessage = 'Error loading matches: ' + error.message;
        this.isLoading = false;
      }
    });
  }

  canUpdateScore(): boolean {
    return this.currentUserRole === 'ADMIN' || this.currentUserRole === 'ORGANIZER';
  }

  updateMatchScore(match: Match): void {
    if (!this.canUpdateScore()) {
      this.errorMessage = 'You do not have permission to update scores';
      return;
    }

    if (match.team1Score == null || match.team2Score == null) {
      this.errorMessage = 'Please enter valid scores for both teams';
      return;
    }

    const scoreUpdate = {
      id: match.id,
      team1Score: match.team1Score,
      team2Score: match.team2Score
    };

    // Call the appropriate API method based on user role
    const updateObservable = this.currentUserRole === 'ADMIN'
      ? this.api.updateMatchScoreAdmin(scoreUpdate)
      : this.api.updateMatchScoreOrganizer(scoreUpdate);

    updateObservable.subscribe({
      next: (updatedMatch) => {
        const index = this.matches.findIndex(m => m.id === match.id);
        if (index !== -1) {
          this.matches[index] = updatedMatch;
        }
        this.errorMessage = '';
      },
      error: (error) => {
        this.errorMessage = 'Error updating score: ' + error.message;
      }
    });
  }

  private getCurrentUserRole(): void {
    // Retrieve user role from localStorage or authentication service
    const userString = localStorage.getItem('currentUser');
    if (userString) {
      const user = JSON.parse(userString);
      this.currentUserRole = user.role;
    }
  }
}
