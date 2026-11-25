import {
  HttpInterceptorFn
} from '@angular/common/http';

import {
  inject
} from '@angular/core';

import {
  Router
} from '@angular/router';

import {
  catchError,
  throwError
} from 'rxjs';

import {
  TokenService
} from '../services/token.service';

export const authInterceptor: HttpInterceptorFn =
  (req, next) => {

    const tokenService =
      inject(TokenService);

    const router =
      inject(Router);

    const token =
      tokenService.getToken();

    const request =
      token
        ? req.clone({
            setHeaders: {
              Authorization:
                `Bearer ${token}`
            }
          })
        : req;

    return next(request)
      .pipe(

        catchError(error => {

          const tokenExpired =
            error?.status === 401 &&
            error?.error?.errorCode ===
              'TOKEN_EXPIRED';

          if (tokenExpired) {

            tokenService.clearToken();

            void router.navigate([
              '/auth/login'
            ]);

          }

          return throwError(
            () => error
          );

        })

      );

  };