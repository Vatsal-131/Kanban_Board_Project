import { Component, OnInit, ViewChild } from '@angular/core';
import { SigninService } from '../services/singin.service';
import { Router } from '@angular/router';
import { NgForm, NgModel } from '@angular/forms';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.css']
})
export class SignInComponent implements OnInit {
  
  signinData = {
    emailId: '',
    password: ''
  };
  errorMessage: string = ''; 
  
  @ViewChild('signinForm') signinForm!: NgForm;
  @ViewChild('email') emailField!: NgModel;

  constructor(
    private signin: SigninService,
    private router: Router
  ) {}

  ngOnInit(): void {
    console.log('Sign-in initialized');
    const token = this.signin.getToken();
    if (token) {
      console.log('Token found. Redirecting to dashboard...');
      this.router.navigate(['/dashboard']);
    }
  }

  formSubmit() {
    // Mark all form fields as touched
    Object.keys(this.signinForm.controls).forEach(field => {
      const control = this.signinForm.controls[field];
      control.markAsTouched();
    });
  
    // Trigger validation for all fields
    this.validateAllFormFields(this.signinForm);
  
    // Check if any field is invalid
    if (this.signinForm.invalid) {
      return;
    }
  
    // Request to server to generate token
    this.signin.generateToken(this.signinData).subscribe(
      (response: any) => {
        const token = response.token;
        const userId = response.userId;
        const emailId = response.emailId;
        this.signin.signinUser(token, emailId, userId);
        console.log('Token received:', token);
        console.log('User email:', emailId);
        console.log('User ID:', userId);
        this.router.navigate(['/dashboard']);
      },
      (error) => {
        console.log("Error occurred while logging in:");
        console.log(error);
        if (error.status === 500) {
          this.errorMessage = 'Email or password is incorrect.';
        } else {
          this.errorMessage = 'An error occurred. Please try again later.';
        }
      }
    );
  }
  
  validateAllFormFields(formGroup: NgForm) {
    Object.keys(formGroup.controls).forEach(field => {
      const control = formGroup.controls[field];
      control.markAsTouched({ onlySelf: true });
    });
  }
  
  emailStartsWithDigit(): boolean {
    return /^\d/.test(this.signinData.emailId);
  }

  emailStartsWithCapital(): boolean {
    return /^[A-Z]/.test(this.signinData.emailId);
  } 
}
