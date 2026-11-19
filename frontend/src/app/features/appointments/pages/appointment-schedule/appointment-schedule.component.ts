import {
  Component,
  OnInit,
  inject,
  signal,
  computed
} from '@angular/core';

import {
  MatCardModule
} from '@angular/material/card';

import {
  AppointmentService
} from '../../services/appointment.service';

import {
  AppointmentResponse
} from '../../models/appointment-response.model';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-appointment-schedule',
  standalone: true,
  imports: [
    RouterLink,
    MatCardModule
  ],
  templateUrl: './appointment-schedule.component.html',
  styleUrl: './appointment-schedule.component.scss'
})
export class AppointmentScheduleComponent
  implements OnInit {

  private readonly appointmentService =
    inject(AppointmentService);

  readonly appointments =
    signal<AppointmentResponse[]>([]);

  readonly groupedAppointments =
    computed(() => {

      const groups =
        new Map<string, AppointmentResponse[]>();

      this.appointments()
        .forEach(appointment => {

          const date =
            appointment.appointmentDate;

          if (!groups.has(date)) {

            groups.set(date, []);

          }

          groups
            .get(date)!
            .push(appointment);

        });

      return Array.from(
        groups.entries()
      )
        .sort(([first], [second]) =>
          first.localeCompare(second)
        );

    });

  ngOnInit(): void {

    this.loadSchedule();

  }

  private loadSchedule(): void {

    this.appointmentService
      .getMyConsultantAppointments()
      .subscribe({

        next: appointments => {

          this.appointments.set(
            appointments
          );

        }

      });

  }

}