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

import {
  BranchService
} from '../../../branches/services/branch.service';


import {
  signal
} from '@angular/core';

import {
  BranchResponse
} from '../../../branches/models/branch-response.model';

import { UserService } from '../../services/user.service';
import { SnackbarService } from '../../../../core/services/snackbar.service';
import { ValidationPatterns } from '../../../../shared/validators/validation.constants';
import { FormErrorUtils } from '../../../../shared/utils/form-error.utils';

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
    MatSelectModule,
    MatSelectModule
  ],
  templateUrl: './user-create.component.html',
  styleUrl: './user-create.component.scss'
})
export class UserCreateComponent {

  private readonly branchService =
    inject(BranchService);
  readonly branches =
    signal<BranchResponse[]>([]);
  private readonly snackbar = inject(SnackbarService);
  private readonly fb =
    inject(FormBuilder);

  private readonly userService =
    inject(UserService);

  private readonly router =
    inject(Router);

  readonly roles = [
    {
      label: 'Administrator',
      value: 'ROLE_ADMIN'
    },
    {
      label: 'Consultant',
      value: 'ROLE_CONSULTANT'
    }
  ];
  protected hidePassword = true;

  readonly form =
    this.fb.group({

      firstName: [
        '',
        [
          Validators.required,
          Validators.maxLength(100)
        ]
      ],

      lastName: [
        '',
        [
          Validators.required,
          Validators.maxLength(100)
        ]
      ],

      email: [
        '',
        [
          Validators.required,
          Validators.email
        ]
      ],

      phoneNumber: [
        '', [
          Validators.required,
          Validators.pattern(
            ValidationPatterns.SOUTH_AFRICAN_PHONE
          )
        ]
      ],

      role: [
        '',
        Validators.required
      ],

      branchId: [''],

      password: [
        '',
        [
          Validators.required,
          Validators.pattern(
            ValidationPatterns.PASSWORD_COMPLEXITY
          )
        ]
      ]

    });

  ngOnInit(): void {

    this.loadBranches();

    this.observeRoleChanges();

    Object.keys(this.form.controls)
      .forEach(controlName => {

        this.form
          .get(controlName)
          ?.valueChanges
          .subscribe(() => {

            FormErrorUtils.clearServerErrors(
              this.form.get(controlName)
            );

          });

      });

  }

  private loadBranches(): void {

    this.branchService
      .getBranches()
      .subscribe({

        next: response => {

          this.branches.set(
            response.items
          );

        }

      });

  }

  private observeRoleChanges(): void {

    this.form.controls.role
      .valueChanges
      .subscribe(role => {

        const branchControl =
          this.form.controls.branchId;

        if (
          role === 'ROLE_CONSULTANT'
        ) {

          branchControl.setValidators(
            Validators.required
          );

        } else {

          branchControl.clearValidators();

          branchControl.setValue('');

        }

        branchControl.updateValueAndValidity();

      });

  }

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

        }, error: error => {

          FormErrorUtils.applyServerErrors(
            this.form,
            error.error?.validationErrors ?? {}
          );

        }

      });

  }

  cancel(): void {

    this.router.navigate([
      '/users'
    ]);

  }

}