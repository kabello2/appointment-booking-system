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
  NotificationService
} from '../../services/notification.service';

import {
  NotificationSummaryResponse
} from '../../models/notification-summary-response.model';

@Component({
  selector: 'app-notification-dashboard',
  standalone: true,
  imports: [
    MatCardModule
  ],
  templateUrl:
    './notification-dashboard.component.html',
  styleUrl:
    './notification-dashboard.component.scss'
})
export class NotificationDashboardComponent
implements OnInit {

  private readonly notificationService =
    inject(NotificationService);

  readonly summary =
    signal<NotificationSummaryResponse | null>(
      null
    );

  ngOnInit(): void {

    this.notificationService
      .getSummary()
      .subscribe({

        next: summary => {

          this.summary.set(summary);

        }

      });

  }

}