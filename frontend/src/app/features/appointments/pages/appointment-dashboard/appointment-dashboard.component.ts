import {
  Component,
  OnInit,
  inject,
  signal
} from '@angular/core';

import {
  MatCardModule
} from '@angular/material/card';

import {
  MatTableModule
} from '@angular/material/table';

import {
  AppointmentService
} from '../../services/appointment.service';

import {
  DashboardResponse
} from '../../models/dashboard-response.model';

import {
  AppointmentResponse
} from '../../models/appointment-response.model';

import { AppointmentStateService } from '../../services/appointment-state.service';

@Component({
  selector: 'app-appointment-dashboard',
  standalone: true,
  imports: [
    MatCardModule,
    MatTableModule
  ],
  templateUrl:
    './appointment-dashboard.component.html',

  styleUrl:
    './appointment-dashboard.component.scss'
})
export class AppointmentDashboardComponent
  implements OnInit {

  private readonly appointmentState =
    inject(
      AppointmentStateService
    );

  private readonly appointmentService =
    inject(AppointmentService);

  readonly dashboard =
    signal<DashboardResponse | null>(
      null
    );

  readonly upcomingAppointments =
    signal<AppointmentResponse[]>([]);

  displayedColumns = [

    'appointmentNumber',

    'appointmentDate',

    'serviceName',

    'consultantName',

    'status'

  ];

  ngOnInit(): void {

    this.loadDashboard();

    this.loadUpcomingAppointments();

  }

  private loadDashboard(): void {

    this.appointmentService
      .getMyDashboard()
      .subscribe({

        next: dashboard => {

          this.dashboard.set(
            dashboard
          );

        }

      });

  }

  private loadUpcomingAppointments(): void {

    this.appointmentService
      .getMyUpcomingAppointments()
      .subscribe({

        next: appointments => {

          this.upcomingAppointments
            .set(appointments);

          this.appointmentState
            .appointments
            .set(appointments);

        }

      });

  }

}