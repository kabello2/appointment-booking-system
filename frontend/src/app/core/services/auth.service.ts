import { Injectable, inject } from '@angular/core';

import { HttpClient } from '@angular/common/http';

import { Router } from '@angular/router';

import { Observable, tap, switchMap } from 'rxjs';

import { environment } from '../../../environments/environment';

import { LoginRequest } from '../models/login-request.model';

import { AuthResponse } from '../models/auth-response.model';
import { AuthUser } from '../models/auth-user.model';

import { TokenService } from './token.service';
import { UserService } from './user.service';
import { AuthState } from '../state/auth.state';

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    private readonly http = inject(HttpClient);

    private readonly router = inject(Router);

    private readonly tokenService = inject(TokenService);
    private readonly userService = inject(UserService);
    private readonly authState = inject(AuthState);


    private readonly authUrl =
        `${environment.apiUrl}/auth`;

    login(
        request: LoginRequest
    ): Observable<AuthUser> {

        return this.http
            .post<AuthResponse>(
                `${this.authUrl}/login`,
                request
            )
            .pipe(

                tap(response => {

                    this.tokenService.saveToken(
                        response.accessToken
                    );

                }),

                switchMap(() =>
                    this.userService
                        .getCurrentUser()
                ),

                tap(user => {

                    this.authState.setUser(
                        user
                    );

                })

            );
    }

    logout(): void {

        this.tokenService.clearToken();

        this.router.navigate([
            '/auth/login'
        ]);

    }

    isAuthenticated(): boolean {

        return this.tokenService
            .isAuthenticated();

    }

}