import { Component, inject } from '@angular/core';

import {
  FormBuilder,
  Validators,
  ReactiveFormsModule
} from '@angular/forms';

import {
  Router
} from '@angular/router';

import {
  MatFormFieldModule
} from '@angular/material/form-field';

import {
  MatInputModule
} from '@angular/material/input';

import {
  MatButtonModule
} from '@angular/material/button';

import {
  ServiceOfferingService
} from '../../services/service-offering.service';
import { SnackbarService } from '../../../../core/services/snackbar.service';

@Component({
  selector: 'app-service-create',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule
  ],
  templateUrl: './service-create.component.html',
  styleUrl: './service-create.component.scss'
})
export class ServiceCreateComponent {

  private readonly snackbar = inject(SnackbarService);
  private readonly fb =
    inject(FormBuilder);

  private readonly router =
    inject(Router);

  private readonly serviceOfferingService =
    inject(ServiceOfferingService);

  readonly form =
    this.fb.group({

      serviceCode: [
        '',
        Validators.required
      ],

      name: [
        '',
        Validators.required
      ],

      description: [''],

      durationMinutes: [
        null,
        [
          Validators.required,
          Validators.min(15)
        ]
      ]

    });

  save(): void {

    if (this.form.invalid) {

      this.form.markAllAsTouched();

      return;

    }

    this.serviceOfferingService
      .createService(
        this.form.getRawValue() as any
      )
      .subscribe({

        next: () => {

            this.snackbar.success(
                'Service offering created successfully'
            );

          this.router.navigate([
            '/services'
          ]);

        }

      });

  }

  cancel(): void {

    this.router.navigate([
      '/services'
    ]);

  }

}