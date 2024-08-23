import { Component, OnInit } from '@angular/core';
import { SigninService } from '../services/singin.service';
import { Router } from '@angular/router';
import { BoardService } from '../services/board.service';
import { Board } from '../interface/board';
import { UserService } from '../services/user.service';
import { NotificationService } from '../services/notification.service';
import { AuthGuard } from '../auth.guard';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  boards: Board[] = [];
  userEmail: string | null = null;
  firstName: string | null = null;
  profilePic: string | null = null; // Added profilePic property
  showNotifications: boolean = false;
  notifications: string[] = [];

  constructor(
    private signin: SigninService,
    private router: Router,
    private boardService: BoardService,
    private userService: UserService,
    private notificationService:NotificationService,
    private authGuard: AuthGuard
  ) { }

  ngOnInit(): void {
    this.authGuard.canActivate();
    console.log('Dashboard initialized');
    const token = localStorage.getItem('token');
    const userEmail = localStorage.getItem('userEmail');
    const userId = localStorage.getItem('userId');
    if (!token) {
      console.log('No token found. Redirecting to login...');
      this.router.navigate(['/login']);
    } else {
      this.userEmail = this.signin.getUserEmail();
      if (this.userEmail) {
        console.log('User email:', this.userEmail);
        this.getBoardsByUserEmail(this.userEmail);
        this.fetchUserDetails(this.userEmail); // Fetch user details including profile pic
      } else {
        console.error('No email ID found in local storage.');
      }
      console.log('Token found:', token);
      console.log('user Email Found:', userEmail);
      console.log('User Id Found:', userId);
      console.log('First Name Found:', this.firstName);
    }
    this.notificationService.notifications$.subscribe((notifications: string[]) => {
      this.notifications = notifications;
    });
  }

  logout() {
    console.log('Logging out...');
    this.signin.logout();
    console.log('Token and user info removed');
    this.router.navigate(['/']);
  }
  toggleNotifications(): void {
    this.showNotifications = !this.showNotifications;
  }

  removeNotification(index: number): void {
    this.notificationService.removeNotification(index);
  }
  removeAllNotifications(): void {
    this.notificationService.removeAllNotifications();
    this.showNotifications = false;
  }
  

  getBoardsByUserEmail(email: string): void {
    this.boardService.getBoardsByUserEmail(email).subscribe(
      boards => {
        this.boards = boards;
        console.log('Boards loaded successfully:', this.boards);
      },
      error => {
        console.error('Error occurred while fetching boards:', error);
      }
    );
  }

  fetchUserDetails(email: string): void {
    this.userService.getUserByEmail(email).subscribe(
      (userData: any) => {
        this.firstName = userData.firstName;
        this.userEmail = userData.emailId;
        this.profilePic = userData.profilePic; // Store profile pic URL
        console.log('User details retrieved successfully:', userData);
      },
      error => {
        console.error('Error fetching user details:', error);
      }
    );
  }
}