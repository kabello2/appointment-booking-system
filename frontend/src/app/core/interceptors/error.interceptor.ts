import {
  HttpErrorResponse,
  HttpInterceptorFn
} from '@angular/common/http';

import {
  inject
} from '@angular/core';

import {
  catchError,
  throwError
} from 'rxjs';

import {
  SnackbarService
} from '../services/snackbar.service';

export const errorInterceptor:
HttpInterceptorFn =
(req, next) => {

  const snackbar =
    inject(SnackbarService);

  return next(req).pipe(

    catchError(
      (error: HttpErrorResponse) => {

        const message =
          error.error?.message
          ||
          'An unexpected error occurred';

        snackbar.error(message);

        return throwError(
          () => error
        );

      }
    )

  );

};