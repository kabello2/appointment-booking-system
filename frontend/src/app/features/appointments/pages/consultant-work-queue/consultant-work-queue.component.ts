import {
  Component,
  OnInit,
  inject,
  signal
} from '@angular/core';

import {
  RouterLink
} from '@angular/router';

import {
  MatTableModule
} from '@angular/material/table';

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
  MatIconModule
} from '@angular/material/icon';

import {
  AppointmentService
} from '../../services/appointment.service';

import {
  AppointmentResponse
} from '../../models/appointment-response.model';

@Component({
  selector: 'app-consultant-work-queue',
  standalone: true,
  imports: [
    RouterLink,
    MatTableModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './consultant-work-queue.component.html',
  styleUrl: './consultant-work-queue.component.scss'
})
export class ConsultantWorkQueueComponent
implements OnInit {

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
    'branchName',
    'status',
    'actions'
  ];

  ngOnInit(): void {

    this.loadAppointments();

  }

  private loadAppointments(): void {

    this.appointmentService
      .getMyConsultantAppointments()
      .subscribe({

        next: appointments => {

          this.allAppointments.set(
            appointments
          );

          this.appointments.set(
            appointments
          );

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

    this.appointments.set(

      this.allAppointments().filter(
        appointment =>

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
            .branchName
            .toLowerCase()
            .includes(value)

      )

    );

  }

}