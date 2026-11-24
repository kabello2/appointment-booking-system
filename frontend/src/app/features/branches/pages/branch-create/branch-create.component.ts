import { Component, inject } from '@angular/core';

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
  BranchService
} from '../../services/branch.service';
import { SnackbarService } from '../../../../core/services/snackbar.service';
import { FormErrorUtils } from '../../../../shared/utils/form-error.utils';

@Component({
  selector: 'app-branch-create',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule
  ],
  templateUrl: './branch-create.component.html',
  styleUrl: './branch-create.component.scss'
})
export class BranchCreateComponent {

  private readonly snackbar = inject(SnackbarService);
  private readonly fb =
    inject(FormBuilder);

  private readonly router =
    inject(Router);

  private readonly branchService =
    inject(BranchService);

  readonly form =
    this.fb.group({

      branchCode: [
        '',
        [
          Validators.required,
          Validators.maxLength(50)
        ]
      ],

      name: [
        '',
        [
          Validators.required,
          Validators.maxLength(255)
        ]
      ],

      addressLine1: [
        '',
        Validators.required
      ],

      addressLine2: [''],

      suburb: [
        '',
        Validators.required
      ],

      city: [
        '',
        Validators.required
      ],

      province: [
        '',
        Validators.required],

      postalCode: [
        '',
        Validators.required],

      contactNumber: [
        '',
        Validators.required]

    });

  ngOnInit(): void {
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

  save(): void {

    if (this.form.invalid) {

      this.form.markAllAsTouched();

      return;

    }

    this.branchService
      .createBranch(
        this.form.getRawValue() as any
      )
      .subscribe({

        next: () => {
          this.snackbar.success(
            'Branch created successfully'
          );


          this.router.navigate([
            '/branches'
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
      '/branches'
    ]);

  }

}