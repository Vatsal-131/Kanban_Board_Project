import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../services/user.service';
import Swal from 'sweetalert2';
import { AbstractControl, NgForm, NgModel, ValidatorFn } from '@angular/forms';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent implements OnInit {
  public user = {
    firstName: '',
    lastName: '',
    emailId: '',
    password: '',
    phoneNumber: '',
  };
  @ViewChild('password') passwordField!: NgModel;
  @ViewChild('signupForm') signupForm!: NgForm;

  constructor(
    private router: Router,
    private userService: UserService,
  ) { }

  ngOnInit(): void { }

    FormSubmit() {
      if (this.signupForm.invalid) {
        this.validateAllFormFields(this.signupForm);
        return;
      }

    this.userService.addUser(this.user).subscribe(
      (data: any) => {
        console.log(data);
        Swal.fire('Success', 'User registered successfully.', 'success');
        this.router.navigate(['/signin']);
      },
      (error) => {
        console.log(error);
        Swal.fire('Error', 'Failed to register user.', 'error');
      }
    );
  }

  validateAllFormFields(formGroup: NgForm) {
    Object.keys(formGroup.controls).forEach(field => {
      const control = formGroup.controls[field];
      control.markAsTouched({ onlySelf: true });
    });
  }

  validatePassword() {
    this.passwordField.control.updateValueAndValidity();
  }

  containsDigits(value: string): boolean {
    return /\d/.test(value);
  }

  containsSpecialCharacters(value: string): boolean {
    return /[^\w\s]/.test(value);
  }

  emailStartsWithDigit(): boolean {
    return /^\d/.test(this.user.emailId);
  }

  emailStartsWithCapital(): boolean {
    return /^[A-Z]/.test(this.user.emailId);
  }

  passwordHasUppercase(): boolean {
    return /[A-Z]/.test(this.user.password);
  }

  passwordHasLowercase(): boolean {
    return /[a-z]/.test(this.user.password);
  }

  passwordHasDigit(): boolean {
    return /[0-9]/.test(this.user.password);
  }

  passwordHasSpecialCharacter(): boolean {
    return /[!@#$%^&*(),.?":{}|<>]/.test(this.user.password);
  }

  startsWithSpace(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
      if (control.value && control.value.trim().startsWith(' ')) {
        return { 'startsWithSpace': true };
      }
      return null;
    };
  }
}
