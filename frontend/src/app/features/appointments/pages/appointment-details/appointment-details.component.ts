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
  MatCardModule
} from '@angular/material/card';

import {
  MatButtonModule
} from '@angular/material/button';

import {
  MatDialog
} from '@angular/material/dialog';

import {
  AppointmentService
} from '../../services/appointment.service';

import {
  AppointmentResponse
} from '../../models/appointment-response.model';

import {
  ConfirmDialogComponent
} from '../../../../shared/components/confirm-dialog/confirm-dialog.component';
import { RouterLink } from '@angular/router';
import { SnackbarService } from '../../../../core/services/snackbar.service';
import { Location }
  from '@angular/common';
import { AppointmentStateService } from '../../services/appointment-state.service';


@Component({
  selector: 'app-appointment-details',
  standalone: true,
  imports: [
    RouterLink,
    MatCardModule,
    MatButtonModule
  ],
  templateUrl: './appointment-details.component.html',
  styleUrl: './appointment-details.component.scss'
})
export class AppointmentDetailsComponent
  implements OnInit {

  private readonly location = inject(Location);
  private readonly snackbar = inject(SnackbarService);
  private readonly route =
    inject(ActivatedRoute);

  private readonly router =
    inject(Router);

  private readonly dialog =
    inject(MatDialog);

  private readonly appointmentService =
    inject(AppointmentService);

  private readonly appointmentState = inject(AppointmentStateService);

  readonly appointment =
    signal<AppointmentResponse | null>(
      null
    );

  private appointmentId!: string;

  ngOnInit(): void {

    this.appointmentId =
      this.route.snapshot.params['id'];

    this.loadAppointment();

  }

  private loadAppointment(): void {

    const selectedAppointment =
      this.appointmentState
        .selectedAppointment();

    if (
      selectedAppointment &&
      selectedAppointment.id ===
      this.appointmentId
    ) {

      this.appointment.set(
        selectedAppointment
      );

      return;

    }

    const cachedAppointment =
      this.appointmentState
        .findAppointment(
          this.appointmentId
        );

    if (cachedAppointment) {

      this.appointment.set(
        cachedAppointment
      );

      this.appointmentState
        .selectAppointment(
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

  confirm(): void {

    const appointment =
      this.appointment();

    if (!appointment) {
      return;
    }

    const dialogRef =
      this.dialog.open(
        ConfirmDialogComponent,
        {
          data: {
            title: 'Confirm Appointment',
            message:
              `Confirm ${appointment.appointmentNumber}?`
          }
        }
      );

    dialogRef
      .afterClosed()
      .subscribe(result => {

        if (!result) {
          return;
        }

        this.appointmentService
          .confirmAppointment(
            appointment.id
          )
          .subscribe(() => {

            this.snackbar.success(
              'Appointment confirmed successfully'
            );

            this.appointmentState.updateAppointment(appointment);

          });

      });

  }

  complete(): void {

    const appointment =
      this.appointment();

    if (!appointment) {
      return;
    }

    const dialogRef =
      this.dialog.open(
        ConfirmDialogComponent,
        {
          data: {
            title: 'Complete Appointment',
            message:
              `Complete ${appointment.appointmentNumber}?`
          }
        }
      );

    dialogRef
      .afterClosed()
      .subscribe(result => {

        if (!result) {
          return;
        }

        this.appointmentService
          .completeAppointment(
            appointment.id
          )
          .subscribe(() => {

            this.snackbar.success(
              'Appointment completed successfully'
            );
            this.appointmentState.updateAppointment(appointment);

          });

      });

  }

  cancel(): void {

    const appointment =
      this.appointment();

    if (!appointment) {
      return;
    }

    const dialogRef =
      this.dialog.open(
        ConfirmDialogComponent,
        {
          data: {
            title: 'Cancel Appointment',
            message:
              `Cancel ${appointment.appointmentNumber}?`
          }
        }
      );

    dialogRef
      .afterClosed()
      .subscribe(result => {

        if (!result) {
          return;
        }

        this.appointmentService
          .cancelAppointment(
            appointment.id
          )
          .subscribe(() => {

            this.snackbar.success(
              'Appointment cancelled successfully'
            );
            this.appointmentState.removeAppointment(appointment.id);

          });

      });

  }

  back(): void {

    this.router.navigate([
      '/appointments'
    ]);

  }

}
