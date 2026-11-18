import {
  Component
}
from '@angular/core';

import {
  ReactiveFormsModule,
  FormBuilder,
  FormGroup,
  Validators
}
from '@angular/forms';

import {
  Router
}
from '@angular/router';

import {
  MatCardModule
}
from '@angular/material/card';

import {
  MatFormFieldModule
}
from '@angular/material/form-field';

import {
  MatInputModule
}
from '@angular/material/input';

import {
  MatButtonModule
}
from '@angular/material/button';

import {
  AuthService
}
from '../../../core/services/auth.service';

import { inject } from '@angular/core';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

  loading = false;

  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  loginForm = this.fb.group({

    email: [
      '',
      [
        Validators.required,
        Validators.email
      ]
    ],

    password: [
      '',
      Validators.required
    ]

  });

  login(): void {

    if (
      this.loginForm.invalid
    ) {
      return;
    }

    this.loading = true;

    this.authService.login(
      this.loginForm.getRawValue() as any
    )
    .subscribe({

      next: () => {

        this.router.navigate([
          '/dashboard'
        ]);

      },

      error: () => {

        this.loading = false;

        alert(
          'Invalid credentials'
        );

      }

    });

  }

}