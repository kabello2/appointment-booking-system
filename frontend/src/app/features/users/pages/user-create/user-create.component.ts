import { Component, inject } from '@angular/core';

import {
  MatIconModule
} from '@angular/material/icon';

import {
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

import {
  Router
} from '@angular/router';

import {
  MatButtonModule
} from '@angular/material/button';

import {
  MatFormFieldModule
} from '@angular/material/form-field';

import {
  MatInputModule
} from '@angular/material/input';

import {
  MatSelectModule
} from '@angular/material/select';

import { UserService } from '../../services/user.service';
import { SnackbarService } from '../../../../core/services/snackbar.service';

@Component({
  selector: 'app-user-create',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatInputModule,
    MatSelectModule
  ],
  templateUrl: './user-create.component.html',
  styleUrl: './user-create.component.scss'
})
export class UserCreateComponent {

  private readonly snackbar = inject(SnackbarService);
  private readonly fb =
    inject(FormBuilder);

  private readonly userService =
    inject(UserService);

  private readonly router =
    inject(Router);

  readonly roles = [
    'ROLE_ADMIN',
    'CONSULTANT',
    'CUSTOMER'
  ];
  protected hidePassword = true;

  readonly form =
    this.fb.group({

      firstName: [
        '',
        Validators.required
      ],

      lastName: [
        '',
        Validators.required
      ],

      email: [
        '',
        [
          Validators.required,
          Validators.email
        ]
      ],

      phoneNumber: [
        '',
        Validators.required
      ],

      role: [
        '',
        Validators.required
      ],

      password: [
        '',
        Validators.required
      ]

    });

  save(): void {

    if (this.form.invalid) {

        this.form.markAllAsTouched();

        return;

    }

    this.userService
      .createUser(
        this.form.getRawValue() as any
      )
      .subscribe({

        next: () => {
            this.snackbar.success(
                'User created successfully'
            );


          this.router.navigate([
            '/users'
          ]);

        }

      });

  }

  cancel(): void {

  this.router.navigate([
    '/users'
  ]);

}

}