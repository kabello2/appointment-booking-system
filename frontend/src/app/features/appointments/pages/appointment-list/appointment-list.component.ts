import {
  Component,
  OnInit,
  inject,
  signal
} from '@angular/core';

import {
  MatTableModule
} from '@angular/material/table';

import {
  AppointmentService
} from '../../services/appointment.service';

import {
  AppointmentResponse
} from '../../models/appointment-response.model';

import { RouterLink } from '@angular/router';

import { MatButtonModule } from '@angular/material/button';

import { MatIconModule } from '@angular/material/icon';

import { MatFormFieldModule } from '@angular/material/form-field';

import { MatInputModule } from '@angular/material/input';

import { MatDialog } from '@angular/material/dialog';

import { ConfirmDialogComponent }
  from '../../../../shared/components/confirm-dialog/confirm-dialog.component';

import { AppointmentStatus }
  from '../../models/appointment-status.enum';

@Component({
  selector: 'app-appointment-list',
  standalone: true,
  imports: [
    MatTableModule,
    RouterLink,

    MatTableModule,

    MatButtonModule,

    MatIconModule,

    MatFormFieldModule,
    MatInputModule
  ],
  templateUrl: './appointment-list.component.html'
})
export class AppointmentListComponent
  implements OnInit {

  private readonly dialog = inject(MatDialog);
  private readonly appointmentService =
    inject(AppointmentService);

  readonly allAppointments =
    signal<AppointmentResponse[]>([]);

  readonly appointments =
    signal<AppointmentResponse[]>([]);

  displayedColumns = [
    'appointmentNumber',
    'appointmentDate',
    'serviceName',
    'consultantName',
    'status',
    'actions'
  ];

  ngOnInit(): void {

    this.loadAppointments();

  }

  private loadAppointments(): void {

    this.appointmentService
      .getAppointments()
      .subscribe({

        next: appointments => {

          this.allAppointments
            .set(appointments);

          this.appointments
            .set(appointments);

        }

      });

  }

  filterAppointments(
    event: Event
  ): void {

    const value =
      (event.target as HTMLInputElement)
        .value
        .toLowerCase();

    const filtered =
      this.allAppointments()
        .filter(appointment =>

          appointment
            .appointmentNumber
            .toLowerCase()
            .includes(value)

          ||

          appointment
            .serviceName
            .toLowerCase()
            .includes(value)

          ||

          appointment
            .consultantName
            ?.toLowerCase()
            .includes(value)

        );

    this.appointments
      .set(filtered);

  }

  confirmAppointment(
    appointment: AppointmentResponse
  ): void {

    const dialogRef =
      this.dialog.open(
        ConfirmDialogComponent,
        {
          width: '400px',

          data: {

            title:
              'Confirm Appointment',

            message:
              `Confirm appointment ${appointment.appointmentNumber}?`

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
          .subscribe({

            next: () => {

              this.loadAppointments();

            }

          });

      });

  }

  completeAppointment(
    appointment: AppointmentResponse
  ): void {

    const dialogRef =
      this.dialog.open(
        ConfirmDialogComponent,

        {
          width: '400px',

          data: {

            title:
              'Complete Appointment',

            message:
              `Mark appointment ${appointment.appointmentNumber} as completed?`

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
          .subscribe({

            next: () => {

              this.loadAppointments();

            }

          });

      });

  }

  cancelAppointment(
    appointment: AppointmentResponse
  ): void {

    const dialogRef =
      this.dialog.open(
        ConfirmDialogComponent,
        {
          width: '400px',

          data: {

            title:
              'Cancel Appointment',

            message:
              `Cancel appointment ${appointment.appointmentNumber}?`

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
          .subscribe({

            next: () => {

              this.loadAppointments();

            }

          });

      });

  }

}