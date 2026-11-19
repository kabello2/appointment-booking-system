import { Injectable, inject } from '@angular/core';

import { HttpClient } from '@angular/common/http';

import { Observable } from 'rxjs';

import { environment } from '../../../../environments/environment';

import { UserResponse } from '../models/user-response.model';

import { CreateUserRequest }
  from '../models/create-user-request.model';

import { UpdateUserRequest }
  from '../models/update-user-request.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private readonly http =
    inject(HttpClient);

  private readonly apiUrl =
    environment.apiUrl;

  getUsers():
    Observable<UserResponse[]> {

    return this.http.get<UserResponse[]>(

      `${this.apiUrl}/admin/users`

    );

  }

  createUser(
    request: CreateUserRequest
  ): Observable<UserResponse> {

    return this.http.post<UserResponse>(

      `${this.apiUrl}/admin/users`,
      request

    );

  }

  updateUser(
    id: string,
    request: UpdateUserRequest
  ) {

    return this.http.put(

      `${this.apiUrl}/admin/users/${id}`,
      request

    );

  }

  enableUser(
    id: string
  ) {

    return this.http.patch(

      `${this.apiUrl}/admin/users/${id}/enable`,
      {}

    );

  }

  disableUser(
    id: string
  ) {

    return this.http.patch(

      `${this.apiUrl}/admin/users/${id}/disable`,
      {}

    );

  }

  getUserById(
    userId: string
  ) {

    return this.http.get<UserResponse>(

      `${this.apiUrl}/admin/users/${userId}`

    );

  }

}
