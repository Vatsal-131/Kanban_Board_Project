// user.service.ts

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../interface/User';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:11111/user';

  constructor(private http: HttpClient) { }

  // Add user
  public addUser(user: any) {
    return this.http.post<any>(`${this.apiUrl}/save`, user);
  }

  getUserById(userId: string): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${userId}`);
  }

  // Fetch user by emailId
  getUserByEmail(emailId: string): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/email/${emailId}`);
  }

  // Fetch all users
  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}`);
  }

  // Update user
  updateUser(userId: string, user: User): Observable<any> {
    return this.http.put(`${this.apiUrl}/${userId}`, user);
  }

  // Delete user
  deleteUser(userId: string): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${userId}`);
  }

  updateUserProfilePic(userId: string, formData: FormData): Observable<any> {
    return this.http.put(`${this.apiUrl}/${userId}/profilePic`, formData);
  }
 
}
