import {
  Component,
  OnInit,
  inject
} from '@angular/core';

import {
  ActivatedRoute,
  Router
} from '@angular/router';

import {
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

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

import {
  UserService
} from '../../services/user.service';
import { SnackbarService } from '../../../../core/services/snackbar.service';
@Component({
  selector: 'app-user-edit',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule
  ],
  templateUrl: './user-edit.component.html',
  styleUrl: './user-edit.component.scss'
})
export class UserEditComponent
implements OnInit {

  private readonly snackbar = inject(SnackbarService);
  private readonly fb =
    inject(FormBuilder);

  private readonly route =
    inject(ActivatedRoute);

  private readonly router =
    inject(Router);

  private readonly userService =
    inject(UserService);

  private userId!: string;

  readonly roles = [
    'ROLE_ADMIN',
    'CONSULTANT',
    'CUSTOMER'
  ];

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
      ]

    });

ngOnInit(): void {

  this.userId =
    this.route.snapshot.params['id'];

  this.loadUser();

}

private loadUser(): void {

  this.userService
    .getUserById(
      this.userId
    )
    .subscribe({

      next: user => {

        this.form.patchValue({

          firstName:
            user.firstName,

          lastName:
            user.lastName,

          email:
            user.email,

          phoneNumber:
            user.phoneNumber,

          role:
            user.role

        });

      }

    });

}

  save(): void {

    if (this.form.invalid) {

      this.form.markAllAsTouched();

      return;

    }

    this.userService
      .updateUser(
        this.userId,
        this.form.getRawValue() as any
      )
      .subscribe({

        next: () => {


            this.snackbar.success(
                'User updated successfully'
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
