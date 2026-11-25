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
  MatButtonModule
} from '@angular/material/button';

import {
  MatIconModule
} from '@angular/material/icon';

import {
  NotificationService
} from '../../services/notification.service';

import {
  NotificationResponse
} from '../../models/notification-response.model';

import {
  MatTableDataSource
} from '@angular/material/table';
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

import { MatDialog } from '@angular/material/dialog';

import { NotificationDetailDialogComponent } from '../../notification-details-dialog/notification-details-dialog.component';

@Component({
  selector: 'app-notification-list',
  standalone: true,
  imports: [
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatPaginatorModule,
    MatSortModule
  ],
  templateUrl:
    './notification-list.component.html',
  styleUrl:
    './notification-list.component.scss'
})
export class NotificationListComponent
  implements OnInit, AfterViewInit {

  private readonly dialog = inject(MatDialog);
  @ViewChild(MatPaginator)
  paginator!: MatPaginator;

  @ViewChild(MatSort)
  sort!: MatSort;

  private readonly notificationService =
    inject(NotificationService);
  
  readonly dataSource =
    new MatTableDataSource<NotificationResponse>();
  totalElements = signal(0);
  totalPages = signal(0);
  pageIndex = signal(0);
  pageSize = signal(10);

  displayedColumns = [

    'recipient',

    'subject',

    'channel',

    'status',

    'actions'

  ];

  ngOnInit(): void {

    this.loadNotifications();

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

    this.loadNotifications();

  }

  private loadNotifications(): void {

    this.notificationService
      .getNotifications(
        this.pageIndex(),
        this.pageSize())
      .subscribe({

        next: response => {
          
          this.dataSource.data =
            response.items;

          this.totalPages.set(
            response.totalPages
          );

          this.totalElements.set(
            response.totalElements
          );

        }

      });

  }

  retry(
    notification: NotificationResponse
  ): void {

    this.notificationService
      .retryNotification(
        notification.id
      )
      .subscribe({

        next: () => {

          this.loadNotifications();

        }

      });

  }
  
    viewNotification(
        notification: NotificationResponse
    ): void {

        this.dialog.open(
            NotificationDetailDialogComponent,
            {
                width: '700px',
                data: notification
            }
        );

    }

}