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

import { ServiceOfferingStateService } from '../../services/service-offering-state.service';

@Component({
  selector: 'app-service-list',
  standalone: true,
  imports: [
    RouterLink,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatFormFieldModule,
    MatPaginatorModule,
    MatSortModule
  ],
  templateUrl: './service-list.component.html',
  styleUrl: './service-list.component.scss'
})
export class ServiceListComponent implements OnInit, AfterViewInit {

  @ViewChild(MatPaginator)
  paginator!: MatPaginator;

  @ViewChild(MatSort)
  sort!: MatSort;
  readonly loading = signal(false);

  private readonly serviceOfferingService =
    inject(ServiceOfferingService);

  private readonly dialog =
    inject(MatDialog);

  private readonly serviceOfferingState =
    inject(ServiceOfferingStateService);

  readonly dataSource =
    new MatTableDataSource<ServiceResponse>();

  totalElements = signal(0);
  totalPages = signal(0);
  pageIndex = signal(0);
  pageSize = signal(10);

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


  ngAfterViewInit(): void {

    this.dataSource.sort =
      this.sort;

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

    this.loadServices();

  }

  private loadServices(): void {

    this.loading.set(true);

    const cachedServices =
      this.serviceOfferingState
        .services();

    if (cachedServices.length > 0) {

      this.dataSource.data = cachedServices;
      return;

    }

    this.serviceOfferingService
      .getServices(
        this.pageIndex(),
        this.pageSize())
      .subscribe(response => {

        this.dataSource.data =
          response.items;

        this.totalPages.set(
          response.totalPages
        );

        this.totalElements.set(
          response.totalElements
        );
        this.loading.set(false);

      });

  }

  filterServices(
    event: Event
  ): void {

    const filterValue =
      (event.target as HTMLInputElement)
        .value;

    this.dataSource.filter =
      filterValue
        .trim()
        .toLowerCase();

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

            this.serviceOfferingState.removeServiceOffering(service.id);

          });

      });

  }

}