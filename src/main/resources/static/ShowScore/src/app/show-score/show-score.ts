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
  currentUserRole: string | null = null;
  showAddMatchForm = false;
  newMatch: Partial<Match> = {
    team1: '',
    team2: '',
    team1Score: 0,
    team2Score: 0,
    matchStatus: 'WAITING',
    sportType: '',
    date: new Date().toISOString().split('T')[0], // Today's date in YYYY-MM-DD format
    startTime: '',
    endTime: ''
  };

  constructor(private api: Api) {}

  ngOnInit(): void {
    this.loadMatches();
    this.getCurrentUserRole();
  }

  toggleAddMatchForm(): void {
    this.showAddMatchForm = !this.showAddMatchForm;
    if (this.showAddMatchForm) {
      // Reset form when opening
      this.newMatch = {
        team1: '',
        team2: '',
        team1Score: 0,
        team2Score: 0,
        matchStatus: 'WAITING',
        sportType: '',
        date: new Date().toISOString().split('T')[0],
        startTime: '',
        endTime: ''
      };
    }
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

  canUpdateScore(match?: Match): boolean {
    // Base permission check
    const hasPermission = this.currentUserRole === 'ADMIN' || this.currentUserRole === 'ORGANIZER';

    // If no match is provided or user doesn't have permission, return the permission check result
    if (!match || !hasPermission) {
      return hasPermission;
    }

    // For FINISHED matches, admins cannot update scores
    if (match.matchStatus === 'FINISHED' && this.currentUserRole === 'ADMIN') {
      return false;
    }

    return true;
  }

  updateMatchScore(match: Match): void {
    if (!this.canUpdateScore(match)) {
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

  createNewMatch(): void {
    if (!this.currentUserRole || this.currentUserRole !== 'ADMIN') {
      this.errorMessage = 'Only administrators can add new matches';
      return;
    }

    // Validate required fields
    if (!this.newMatch.team1 || !this.newMatch.team2 || !this.newMatch.sportType ||
        !this.newMatch.date || !this.newMatch.startTime) {
      this.errorMessage = 'Please fill in all required fields';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    this.api.createMatch(this.newMatch).subscribe({
      next: (createdMatch) => {
        // Add the new match to the list
        this.matches.unshift(createdMatch);
        this.isLoading = false;
        this.showAddMatchForm = false; // Hide the form after successful creation

        // Optional: Show success message
        this.errorMessage = ''; // Clear any previous error
      },
      error: (error) => {
        this.errorMessage = 'Error creating match: ' + error.message;
        this.isLoading = false;
      }
    });
  }
}
