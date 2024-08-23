import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user.service';
import { User } from '../interface/User';
import { SigninService } from '../services/singin.service';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthGuard } from '../auth.guard';


@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  styleUrls: ['./user-details.component.css']
})
export class UserDetailsComponent implements OnInit {

  loggedInUser: User | undefined;
  editProfilePicMode: boolean = false; // Edit mode for profile picture
  editUserDetailsMode: boolean = false; // Edit mode for user details
  profileImagePreview: string | ArrayBuffer | null = null; // Variable to hold preview of selected profile image
  userEmail: string | null = null;
  firstName: string | null = null;
  profilePic: string | null = null;
  userDetailsForm!: FormGroup;

  constructor(private formBuilder: FormBuilder,private userService: UserService, private signinService: SigninService, private router: Router, private authGuard: AuthGuard) { }

  ngOnInit(): void {
    this.authGuard.canActivate();
    this.loadLoggedInUser();
    this.initUserDetailsForm();
    const userEmail = this.signinService.getUserEmail();
  if (userEmail) {
    this.fetchUserDetails(userEmail);
  }
  }
  initUserDetailsForm() {
    this.userDetailsForm = this.formBuilder.group({
      firstName: ['', [Validators.required, Validators.pattern('^[a-zA-Z]*$')]],
      lastName: ['', [Validators.required, Validators.pattern('^[a-zA-Z]*$')]],
      phoneNumber: ['', [Validators.required, Validators.pattern('^6|7|8|9[0-9]{9}$')]],
    });
  }

  logout() {
    console.log('Logging out...');
    this.signinService.logout();
    console.log('Token and user info removed');
    this.router.navigate(['/']);
  }

  loadLoggedInUser() {
    const userId = this.signinService.getUserId();
    if (userId) {
      this.userService.getUserById(userId).subscribe(
        (user: User) => {
          this.loggedInUser = user;
        },
        (error) => {
          console.log(error);
        }
      );
    } else {
      console.log("User ID not found.");
    }
  }

  toggleProfilePicEditMode() {
    this.editProfilePicMode = !this.editProfilePicMode;
  }

  toggleUserDetailsEditMode() {
    this.editUserDetailsMode = !this.editUserDetailsMode;
  }

  saveProfilePic() {
    if (this.loggedInUser && this.profileImagePreview) {
      const formData = new FormData();
      formData.append('profilePic', this.profileImagePreview as 'profile.jpg');
      this.userService.updateUserProfilePic(this.loggedInUser.userId, formData).subscribe(
        (response) => {
          console.log("Profile picture updated successfully.");
          this.editProfilePicMode = false;
          // Reload the page to reflect the changes
          location.reload();
        },
        (error) => {
          console.log("Error updating profile picture:", error);
        }
      );
    } else {
      console.log("Logged-in user details or profile picture not found.");
    }
  }
  

  saveUserDetails() {
    if (this.loggedInUser) {
      this.userService.updateUser(this.loggedInUser.userId, this.loggedInUser).subscribe(
        (response) => {
          console.log("User details updated successfully.");
          this.editUserDetailsMode = false;
        },
        (error) => {
          console.log("Error updating user details:", error);
        }
      );
    } else {
      console.log("Logged-in user details not found.");
    }
  }

  cancelProfilePicEdit() {
    // Reset profile pic edit mode
    this.editProfilePicMode = false;
    // Reset profile pic preview
    this.profileImagePreview = null;
  }

  cancelUserDetailsEdit() {
    // Reset user details edit mode
    this.editUserDetailsMode = false;
    // Load original user details
    this.loadLoggedInUser();
  }

  onProfilePicSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      // Read the selected file as data URL and display its preview
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => {
        this.profileImagePreview = reader.result;
      };
    }
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