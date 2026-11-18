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

@Component({
  selector: 'app-notification-list',
  standalone: true,
  imports: [
    MatTableModule,
    MatButtonModule,
    MatIconModule
  ],
  templateUrl:
    './notification-list.component.html',
  styleUrl:
    './notification-list.component.scss'
})
export class NotificationListComponent
implements OnInit {

  private readonly notificationService =
    inject(NotificationService);

  readonly notifications =
    signal<NotificationResponse[]>([]);

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

  private loadNotifications(): void {

    this.notificationService
      .getNotifications()
      .subscribe({

        next: notifications => {

          this.notifications
            .set(notifications);

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

}