import { ApplicationConfig, provideBrowserGlobalErrorListeners, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { authInterceptor } from './core/interceptors/auth.interceptor';
import { errorInterceptor } from './core/interceptors/error.interceptor';
import {
  provideAnimations
} from '@angular/platform-browser/animations';

import {
  MatSnackBarModule
} from '@angular/material/snack-bar';

export const appConfig: ApplicationConfig = {

  providers: [
    provideAnimations(),

    provideBrowserGlobalErrorListeners(),

    provideZoneChangeDetection({
      eventCoalescing: true
    }),

    provideRouter(routes),

    provideHttpClient(
      withInterceptors([
        authInterceptor,
        errorInterceptor
    ])
)

  ]

};