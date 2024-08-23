import { Component } from '@angular/core';
import { SigninService } from '../services/singin.service';
@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css']
})
export class FooterComponent {
  loggedIn: boolean;

  constructor(private signinService: SigninService) {
    // Initialize loggedIn based on the authentication status
    this.loggedIn = this.signinService.isLoggedIn();
  }
}
