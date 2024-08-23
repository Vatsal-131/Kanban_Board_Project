// import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  private baseUrl = 'http://localhost:33333/task';

  constructor(private http: HttpClient) { }

  createTask(taskData: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/create`, taskData);
  }
}