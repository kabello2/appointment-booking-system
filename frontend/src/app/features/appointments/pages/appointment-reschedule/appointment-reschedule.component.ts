import {
  Component,
  OnInit,
  inject,
  signal
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
  AppointmentService
} from '../../services/appointment.service';

import {
  AppointmentStateService
} from '../../services/appointment-state.service';

import {
  AvailableSlotResponse
} from '../../models/available-slot-response.model';

import {
  AppointmentResponse
} from '../../models/appointment-response.model';

import {
  SnackbarService
} from '../../../../core/services/snackbar.service';

@Component({
  selector: 'app-appointment-reschedule',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule
  ],
  templateUrl:
    './appointment-reschedule.component.html',
  styleUrl:
    './appointment-reschedule.component.scss'
})
export class AppointmentRescheduleComponent
  implements OnInit {

  private readonly snackbar =
    inject(SnackbarService);

  private readonly fb =
    inject(FormBuilder);

  private readonly route =
    inject(ActivatedRoute);

  private readonly router =
    inject(Router);

  private readonly appointmentService =
    inject(AppointmentService);

  private readonly appointmentState =
    inject(AppointmentStateService);

  private appointmentId!: string;

  readonly availableSlots =
    signal<AvailableSlotResponse[]>([]);

  readonly appointment =
    signal<AppointmentResponse | null>(
      null
    );

  readonly form =
    this.fb.group({

      appointmentDate: [
        '',
        Validators.required
      ],

      startTime: [
        '',
        Validators.required
      ]

    });

  ngOnInit(): void {

    this.appointmentId =
      this.route.snapshot.paramMap.get(
        'id'
      )!;

    this.loadAppointment();

    this.form.controls.appointmentDate
      .valueChanges
      .subscribe(date => {

        if (!date) {
          return;
        }

        this.form.controls.startTime
          .setValue('');

        this.loadAvailableSlots(date);

      });

  }

  private loadAppointment(): void {

    const cachedAppointment =
      this.appointmentState
        .findAppointment(
          this.appointmentId
        );

    if (cachedAppointment) {

      this.appointment.set(
        cachedAppointment
      );

      return;

    }

    this.appointmentService
      .getAppointment(
        this.appointmentId
      )
      .subscribe({

        next: appointment => {

          this.appointment.set(
            appointment
          );

          this.appointmentState
            .selectAppointment(
              appointment
            );

        }

      });

  }

  private loadAvailableSlots(
    appointmentDate: string
  ): void {

    const appointment =
      this.appointment();

    if (!appointment) {
      return;
    }

    this.appointmentService
      .getAvailableSlots(
        appointment.branchId,
        appointmentDate
      )
      .subscribe({

        next: slots => {

          this.availableSlots.set(
            slots
          );

        }

      });

  }

  save(): void {

    if (this.form.invalid) {

      this.form.markAllAsTouched();

      return;

    }

    this.appointmentService
      .rescheduleAppointment(

        this.appointmentId,

        this.form.getRawValue() as any

      )
      .subscribe({

        next: () => {

          this.snackbar.success(
            'Appointment rescheduled successfully'
          );

          this.router.navigate([
            '/appointments',
            this.appointmentId
          ]);

        }

      });

  }

  back(): void {

    this.router.navigate([
      '/appointments',
      this.appointmentId
    ]);

  }

}