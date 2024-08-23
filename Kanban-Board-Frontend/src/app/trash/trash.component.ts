import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { SigninService } from '../services/singin.service';
import { NotificationService } from '../services/notification.service';
import { UserService } from '../services/user.service';
import { Router } from '@angular/router';
import { AuthGuard } from '../auth.guard';


@Component({
  selector: 'app-trash',
  templateUrl: './trash.component.html',
  styleUrls: ['./trash.component.css']
})
export class TrashComponent implements OnInit {

  deletedTasks: any[] = [];
  showDeletedTasks: boolean = false; // Variable to control the visibility of deleted tasks table
  userEmail: string | null = null;
  firstName: string | null = null;
  profilePic: string | null = null;

  constructor(private http: HttpClient, private signinService: SigninService,
    private notificationService: NotificationService, private userService: UserService, private router: Router, private authGuard: AuthGuard) { }

  ngOnInit(): void {
    // Fetch deleted tasks initially
    this.getDeletedTasks();
    this.authGuard.canActivate();

    // Fetch user details
  const userEmail = this.signinService.getUserEmail();
  if (userEmail) {
    this.fetchUserDetails(userEmail);
  }
  }

  logout() {
    console.log('Logging out...');
    this.signinService.logout();
    console.log('Token and user info removed');
    this.router.navigate(['/']);
  }

  getDeletedTasks(): void {
    // Fetch the user ID from the SigninService
    const userId = this.signinService.getUserId();
    if (!userId) {
      console.error('User ID not found. Unable to fetch deleted tasks.');
      return;
    }

    // Make an HTTP GET request to fetch deleted tasks for the specific user
    this.http.get<any[]>(`http://localhost:33333/task/deletedtask/${userId}`, { responseType: 'json' })
      .subscribe(
        (tasks) => {
          console.log('Deleted tasks fetched:', tasks);
          this.deletedTasks = tasks;
        },
        (error) => {
          console.error('Error fetching deleted tasks:', error);
        }
      );
  }

  toggleDeletedTasks(): void {
    // Toggle the visibility of the deleted tasks table
    this.showDeletedTasks = !this.showDeletedTasks;
  }

  restoreTask(taskId: string, taskName: string): void {
    const url = `http://localhost:33333/task/restore/${taskId}`;
  
    this.http.post(url, {}, { responseType: 'text' }).subscribe(
      (response) => {
        console.log('Task restoration response:', response);
        if (response && response.includes('successfully')) {
          console.log('Task restored successfully:', response);
          // Remove the restored task from the deletedTasks array
          this.deletedTasks = this.deletedTasks.filter(task => task.id !== taskId);
          // Fetch the updated list of deleted tasks immediately after restoration
          this.getDeletedTasks();
          // Refresh the window
          window.location.reload();
          this.notificationService.addNotification(`The task "${taskName}" has been successfully restored.`);

        } else {
          console.error('Unexpected response when restoring task:', response);
        }
      },
      (error) => {
        console.error('Error restoring task:', error);
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