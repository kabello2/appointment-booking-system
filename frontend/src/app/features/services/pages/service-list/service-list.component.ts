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
  MatButtonModule
} from '@angular/material/button';

import {
  MatIconModule
} from '@angular/material/icon';

import {
  MatInputModule
} from '@angular/material/input';

import {
  MatFormFieldModule
} from '@angular/material/form-field';

import {
  MatDialog
} from '@angular/material/dialog';

import {
  ServiceOfferingService
} from '../../services/service-offering.service';

import {
  ServiceResponse
} from '../../models/service-response.model';

import {
  ConfirmDialogComponent
} from '../../../../shared/components/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-service-list',
  standalone: true,
  imports: [
    RouterLink,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatFormFieldModule
  ],
  templateUrl: './service-list.component.html',
  styleUrl: './service-list.component.scss'
})
export class ServiceListComponent implements OnInit {

  private readonly serviceOfferingService =
    inject(ServiceOfferingService);

  private readonly dialog =
    inject(MatDialog);

  readonly allServices =
    signal<ServiceResponse[]>([]);

  readonly services =
    signal<ServiceResponse[]>([]);

  displayedColumns = [
    'serviceCode',
    'name',
    'durationMinutes',
    'active',
    'actions'
  ];

  ngOnInit(): void {

    this.loadServices();

  }

  private loadServices(): void {

    this.serviceOfferingService
      .getServices()
      .subscribe(services => {

        this.allServices.set(services);

        this.services.set(services);

      });

  }

  filterServices(
    event: Event
  ): void {

    const value =
      (event.target as HTMLInputElement)
        .value
        .toLowerCase();

    this.services.set(

      this.allServices().filter(service =>

        service.serviceCode
          .toLowerCase()
          .includes(value)

        ||

        service.name
          .toLowerCase()
          .includes(value)

      )

    );

  }

  deleteService(
    service: ServiceResponse
  ): void {

    const dialogRef =
      this.dialog.open(
        ConfirmDialogComponent,
        {
          width: '400px',

          data: {
            title: 'Delete Service',
            message:
              `Delete ${service.name}?`
          }
        }
      );

    dialogRef
      .afterClosed()
      .subscribe(result => {

        if (!result) {
          return;
        }

        this.serviceOfferingService
          .deleteService(service.id)
          .subscribe(() => {

            this.loadServices();

          });

      });

  }

}