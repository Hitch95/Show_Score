import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { User } from '../models/user';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = '/api/auth';
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) {
    // Check if the user is already logged in at startup
    const savedUser = localStorage.getItem('currentUser');
    if (savedUser) {
      this.currentUserSubject.next(JSON.parse(savedUser));
    }
  }

  login(username: string, password: string): Observable<User> {
    return this.http.post<any>(`${this.baseUrl}/login`, { username, password }).pipe(
      tap(response => {
        console.log('Raw response:', response);
        const user = response.user;
        const token = response.accessToken;

        if (user.role === 'ADMIN' || user.role === 'ORGANIZER') {
          console.log('User role:', user.role, 'Type:', typeof user.role);
          localStorage.setItem('currentUser', JSON.stringify(user));
          localStorage.setItem('authToken', token);
          this.currentUserSubject.next(user);
        } else {
          throw new Error('Access denied. Only administrators and organizers can log in.');
        }
      }),
      map(response => response.user)
    );
  }

  logout(): void {
    localStorage.removeItem('currentUser');
    localStorage.removeItem('authCredentials');
    this.currentUserSubject.next(null);
  }

  get currentUserValue(): User | null {
    return this.currentUserSubject.value;
  }

  isLoggedIn(): boolean {
    return !!this.currentUserValue;
  }

  isAdminOrOrganizer(): boolean {
    const user = this.currentUserValue;
    return user?.role === 'ADMIN' || user?.role === 'ORGANIZER';
  }

  getAuthHeaders(): HttpHeaders {
    const credentials = localStorage.getItem('authCredentials');
    if (credentials) {
      return new HttpHeaders({
        'Authorization': 'Basic ' + credentials
      });
    }
    return new HttpHeaders();
  }
}
