import { Component, signal, inject, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';

import { TokenService } from './core/services/token.service';
import { UserService } from './core/services/user.service';
import { AuthState } from './core/state/auth.state';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected readonly title = signal('appointment-booking-ui');
  private readonly tokenService = inject(TokenService);
  private readonly userService = inject(UserService);
  private readonly authState = inject(AuthState);

  ngOnInit(): void {

  if (
    !this.tokenService.isAuthenticated()
  ) {
    return;
  }

  this.userService
    .getCurrentUser()
    .subscribe({

      next: user => {

        this.authState.setUser(
          user
        );

      },

      error: () => {

        this.tokenService.clearToken();

      }

    });

    console.log(
  this.authState.currentUser()
);


}

}
