import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';

import { AuthUser } from '../models/auth-user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl = environment.apiUrl;

  getCurrentUser(): Observable<AuthUser> {

    return this.http.get<AuthUser>(
      `${this.apiUrl}/admin/users/current`
    );

  }

}
