import {
  Component,
  OnInit,
  inject,
  signal
} from '@angular/core';

import {
  ReactiveFormsModule,
  FormBuilder,
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
  AppointmentService
} from '../../services/appointment.service';

import {
  BranchService
} from '../../../branches/services/branch.service';

import {
  ServiceOfferingService
} from '../../../services/services/service-offering.service';

import {
  BranchResponse
} from '../../../branches/models/branch-response.model';

import {
  ServiceResponse
} from '../../../services/models/service-response.model';

import {
  AvailableSlotResponse
} from '../../models/available-slot-response.model';
import { SnackbarService } from '../../../../core/services/snackbar.service';
import { FormErrorUtils } from '../../../../shared/utils/form-error.utils';

@Component({
  selector: 'app-appointment-create',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule
  ],
  templateUrl: './appointment-create.component.html',
  styleUrl: './appointment-create.component.scss'
})
export class AppointmentCreateComponent
  implements OnInit {

  private readonly snackbar = inject(SnackbarService);
  private readonly fb = inject(FormBuilder);

  private readonly router = inject(Router);

  private readonly appointmentService =
    inject(AppointmentService);

  private readonly branchService =
    inject(BranchService);

  private readonly serviceOfferingService =
    inject(ServiceOfferingService);

  readonly branches =
    signal<BranchResponse[]>([]);

  readonly services =
    signal<ServiceResponse[]>([]);

  readonly availableSlots =
    signal<AvailableSlotResponse[]>([]);

  readonly form =
    this.fb.group({

      branchId: [
        '',
        Validators.required
      ],

      serviceOfferingId: [
        '',
        Validators.required
      ],

      appointmentDate: [
        '',
        Validators.required
      ],

      startTime: [
        '',
        Validators.required
      ],

      notes: ['']

    });

  ngOnInit(): void {

    this.loadBranches();

    this.loadServices();

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
      .subscribe(branches => {

        this.branches.set(branches);

      });

  }

  private loadServices(): void {

    this.serviceOfferingService
      .getServices()
      .subscribe(services => {

        this.services.set(services);

      });

  }

  loadSlots(): void {

    const branchId =
      this.form.controls.branchId.value;

    const appointmentDate =
      this.form.controls.appointmentDate.value;

    if (
      !branchId ||
      !appointmentDate
    ) {
      return;
    }

    this.appointmentService
      .getAvailableSlots(
        branchId,
        appointmentDate
      )
      .subscribe(slots => {

        this.availableSlots
          .set(slots);

      });

  }


  save(): void {

    if (this.form.invalid) {

      this.form.markAllAsTouched();

      return;

    }

    this.appointmentService
      .bookAppointment(
        this.form.getRawValue() as any
      )
      .subscribe({

        next: () => {
          this.snackbar.success(
            'Appointment created successfully'
          );


          this.router.navigate([
            '/appointments'
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
      '/appointments'
    ]);

  }

}