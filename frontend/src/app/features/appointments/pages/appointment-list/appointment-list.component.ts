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

import { AppointmentStateService } from '../../services/appointment-state.service';

import { Router, ActivatedRoute } from '@angular/router';

import {
  ViewChild,
  AfterViewInit
} from '@angular/core';

import {
  MatPaginator,
  MatPaginatorModule
} from '@angular/material/paginator';

import {
  MatSort,
  MatSortModule
} from '@angular/material/sort';

import { MatTableDataSource } from '@angular/material/table';

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
    MatInputModule,
    MatPaginatorModule,
    MatSortModule,
    MatSort
  ],
  templateUrl: './appointment-list.component.html'
})
export class AppointmentListComponent
  implements OnInit, AfterViewInit  {

  @ViewChild(MatPaginator)
  paginator!: MatPaginator;

  @ViewChild(MatSort)
  sort!: MatSort;
  private readonly route = inject(ActivatedRoute);
  private readonly dialog = inject(MatDialog);
  private readonly router = inject(Router);

  readonly mode = signal<'CUSTOMER' | 'CONSULTANT'>('CUSTOMER');

  private readonly appointmentState =
    inject(AppointmentStateService);
  private readonly appointmentService =
    inject(AppointmentService);

  readonly dataSource =
    new MatTableDataSource<AppointmentResponse>();

  displayedColumns = [
    'appointmentNumber',
    'appointmentDate',
    'serviceName',
    'consultantName',
    'status',
    'actions'
  ];

  ngOnInit(): void {

    const mode = this.route.snapshot.data['mode'];
    this.mode.set(mode ?? 'CUSTOMER');
    this.mode.name;
   
    this.loadAppointments();

  }

  
  ngAfterViewInit(): void {

    this.dataSource.paginator =
      this.paginator;

    this.dataSource.sort =
      this.sort;

  }

  private loadAppointments(): void {

    const cachedAppointments =
      this.appointmentState
        .appointments();

    if (cachedAppointments.length > 0) {

      this.dataSource.data = cachedAppointments;
      return;

    }

    this.appointmentService
      .getAppointments()
      .subscribe({

        next: appointments => {

          this.dataSource.data = appointments;

          this.appointmentState
            .appointments
            .set(appointments);

        }

      });

  }

  viewAppointment(
    appointment: AppointmentResponse
  ): void {

    this.appointmentState
      .selectedAppointment
      .set(appointment);

    this.router.navigate([
      '/appointments',
      appointment.id
    ]);

  }

  filterAppointments(
    event: Event
  ): void {

    const value =
      (event.target as HTMLInputElement)
        .value
        .toLowerCase();

    
    const filterValue =
      (event.target as HTMLInputElement)
        .value;

    this.dataSource.filter =
      filterValue
        .trim()
        .toLowerCase();

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

              this.appointmentState.updateAppointment(appointment);

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

              this.appointmentState.updateAppointment(appointment);

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

              this.appointmentState.updateAppointment(appointment);

            }

          });

      });

  }

}