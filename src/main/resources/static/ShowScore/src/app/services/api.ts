import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import { Observable } from 'rxjs';
import { Match } from '../models/match';

@Injectable({
  providedIn: 'root'
})
export class Api {
  private baseUrl = '/api/public/matches'
  private adminBaseUrl = '/api/admin/matches';
  private organizerBaseUrl = '/api/organizer/matches';

  // Constructor to inject HttpClient
  constructor(private http: HttpClient) {}

  // Get all matches
  getMatches(): Observable<Match[]> {
    return this.http.get<Match[]>(this.baseUrl);
  }
  // Get match by ID
  getMatchById(id: number): Observable<Match> {
    return this.http.get<Match>(`${this.baseUrl}/${id}`);
  }

  // Update match score for ADMIN
  updateMatchScoreAdmin(scoreUpdate: {id: number, team1Score: number, team2Score: number}): Observable<Match> {
    const params = new HttpParams()
      .set('id', scoreUpdate.id.toString())
      .set('team1Score', scoreUpdate.team1Score.toString())
      .set('team2Score', scoreUpdate.team2Score.toString());

    return this.http.put<Match>(`${this.adminBaseUrl}/updateScore`, null, { params });
  }

// Update match score for ORGANIZER
  updateMatchScoreOrganizer(scoreUpdate: {id: number, team1Score: number, team2Score: number}): Observable<Match> {
    const params = new HttpParams()
      .set('id', scoreUpdate.id.toString())
      .set('team1Score', scoreUpdate.team1Score.toString())
      .set('team2Score', scoreUpdate.team2Score.toString());

    return this.http.put<Match>(`${this.organizerBaseUrl}/updateScore`, null, { params });
  }

  // Keep the original method for compatibility
  updateMatchScore(match: Match): Observable<Match> {
    return this.http.put<Match>(`${this.baseUrl}/${match.id}`, match);
  }
}
