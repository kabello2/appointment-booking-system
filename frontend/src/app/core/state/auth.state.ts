import {
  Injectable,
  signal,
  computed
} from '@angular/core';

import {
  AuthUser
} from '../models/auth-user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthState {

  readonly currentUser =
    signal<AuthUser | null>(null);

  readonly isLoggedIn =
    computed(() =>
      this.currentUser() !== null
    );

  setUser(
    user: AuthUser
  ): void {

    this.currentUser.set(user);

  }

  clear(): void {

    this.currentUser.set(null);

  }

}