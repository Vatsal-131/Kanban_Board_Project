// signin.service.ts

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SigninService {
  private apiUrl = "http://localhost:22222/authdata";
  private tokenKey = 'token'; // Key for storing token in local storage
  private userEmailKey = 'userEmail'; // Key for storing user email in local storage
  private userIdKey = 'userId'; // Key for storing user ID in local storage

  constructor(private http: HttpClient) { }

  // Generate token
  public generateToken(signinData: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, signinData, { responseType: 'json' });
  }

  
 // Save token, user ID, and user email in local storage
public signinUser(token: string, userEmail: string, userId: string): void {
  localStorage.setItem(this.tokenKey, token);
  localStorage.setItem(this.userEmailKey, userEmail);
  localStorage.setItem(this.userIdKey, userId);
}


  // Get user email from local storage
  public getUserEmail(): string | null {
    return localStorage.getItem(this.userEmailKey);
  }

  // Get user ID from local storage
  public getUserId(): string | null {
    return localStorage.getItem(this.userIdKey);
  }

  // Check if user is logged in
  public isLoggedIn(): boolean {
    const tokenstr = localStorage.getItem(this.tokenKey);
    return tokenstr !== null;
  }

  // Logout: remove token, user email, and user ID from local storage
  public logout(): void {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.userEmailKey);
    localStorage.removeItem(this.userIdKey);
  }

  // Get token
  public getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }
}