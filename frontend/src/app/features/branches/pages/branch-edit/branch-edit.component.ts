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
  BranchService
} from '../../services/branch.service';
import { SnackbarService } from '../../../../core/services/snackbar.service';

@Component({
  selector: 'app-branch-edit',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule
  ],
  templateUrl: './branch-edit.component.html',
  styleUrl: './branch-edit.component.scss'
})
export class BranchEditComponent implements OnInit {

  private readonly snackbar = inject(SnackbarService);
  private readonly fb =
    inject(FormBuilder);

  private readonly router =
    inject(Router);

  private readonly route =
    inject(ActivatedRoute);

  private readonly branchService =
    inject(BranchService);

  private branchId!: string;

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

      addressLine1: [''],

      addressLine2: [''],

      suburb: [''],

      city: [''],

      province: [''],

      postalCode: [''],

      contactNumber: ['']

    });

  ngOnInit(): void {

    this.branchId =
      this.route.snapshot.params['id'];

    this.loadBranch();

  }

  private loadBranch(): void {

    this.branchService
      .getBranchById(this.branchId)
      .subscribe({

        next: branch => {

          this.form.patchValue({

            branchCode:
              branch.branchCode,

            name:
              branch.name,

            addressLine1:
              branch.addressLine1,

            addressLine2:
              branch.addressLine2,

            suburb:
              branch.suburb,

            city:
              branch.city,

            province:
              branch.province,

            postalCode:
              branch.postalCode,

            contactNumber:
              branch.contactNumber

          });

        }

      });

  }

  save(): void {

    if (this.form.invalid) {

      this.form.markAllAsTouched();

      return;

    }

    this.branchService
      .updateBranch(
        this.branchId,
        this.form.getRawValue() as any
      )
      .subscribe({

        next: () => {

            this.snackbar.success(
                'Branch updated successfully'
            );

          this.router.navigate([
            '/branches'
          ]);

        }

      });

  }

  cancel(): void {

    this.router.navigate([
      '/branches'
    ]);

  }

}