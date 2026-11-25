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

import {
  MatPaginator,
  MatPaginatorModule
} from '@angular/material/paginator';

import {
  ViewChild,
  AfterViewInit
} from '@angular/core';

@Component({
  selector: 'app-consultant-work-queue',
  standalone: true,
  imports: [
    RouterLink,
    MatTableModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatPaginatorModule
  ],
  templateUrl: './consultant-work-queue.component.html',
  styleUrl: './consultant-work-queue.component.scss'
})
export class ConsultantWorkQueueComponent
implements OnInit, AfterViewInit {

  @ViewChild(MatPaginator)
  paginator!: MatPaginator;

  private readonly appointmentService =
    inject(AppointmentService);

  readonly allAppointments =
    signal<AppointmentResponse[]>([]);

  readonly appointments =
    signal<AppointmentResponse[]>([]);

  totalElements = signal(0);
  totalPages = signal(0);
  pageIndex = signal(0);
  pageSize = signal(10);

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

  ngAfterViewInit(): void {

    this.paginator.page.subscribe(
      () => this.onPageChange()
    );

  }

  onPageChange(): void {

    this.pageIndex.set(
      this.paginator.pageIndex
    );

    this.pageSize.set(
      this.paginator.pageSize
    );

    this.loadAppointments();

  }

  private loadAppointments(): void {

    this.appointmentService
      .getAppointments(
        this.pageIndex(),
        this.pageSize())
      .subscribe({

        next: response => {

          this.allAppointments.set(
            response.items
          );

          this.appointments.set(
            response.items
          );

          this.totalPages.set(
            response.totalPages
          );

          this.totalElements.set(
            response.totalElements
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