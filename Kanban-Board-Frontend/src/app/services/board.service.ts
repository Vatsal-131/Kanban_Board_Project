import { Injectable, EventEmitter } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Board } from '../interface/board';
import { Task } from '../interface/task';

@Injectable({
  providedIn: 'root'
})
export class BoardService {
  private apiUrl = 'http://localhost:44444/board';
  boardSelected = new EventEmitter<Board | null>();

  constructor(private http: HttpClient) {}

  getBoardsByUserId(userId: string): Observable<Board[]> {
    return this.http.get<Board[]>(`${this.apiUrl}/user/${userId}`);
  }

  getBoardsByUserEmail(emailId: string): Observable<Board[]> {
    return this.http.get<Board[]>(`${this.apiUrl}/user/email/${emailId}`);
  }

  createBoard(userId: string, board: Board): Observable<Board> {
    return this.http.post<Board>(`${this.apiUrl}/create`, board);
  }

  updateBoard(board: Board): Observable<Board> {
    const { boardId, boardTitle } = board;
    return this.http.put<Board>(`${this.apiUrl}/${boardId}/update`, { boardId, boardTitle });
  }

  deleteBoard(boardId: string): Observable<string> {
    return this.http.delete<string>(`${this.apiUrl}/${boardId}/delete`);
  }

  getTasksOfBoard(boardId: string): Observable<Task[]> {
    return this.http.get<Task[]>(`${this.apiUrl}/${boardId}`); // Adjust the endpoint to fetch tasks of a specific board
  }
  getBoards(): Observable<Board[]> {
    return this.http.get<Board[]>(this.apiUrl);
  }

  getBoardsByAssigneeEmail(assigneeEmail: string): Observable<Board[]> {
    return this.http.get<Board[]>(`${this.apiUrl}/assignee/${assigneeEmail}`);
}

}