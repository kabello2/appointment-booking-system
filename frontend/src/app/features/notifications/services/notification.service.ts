import { Injectable, inject } from '@angular/core';

import { HttpClient } from '@angular/common/http';

import { environment } from '../../../../environments/environment';

import {
  NotificationResponse
} from '../models/notification-response.model';

import {
  NotificationSummaryResponse
} from '../models/notification-summary-response.model';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  private readonly http =
    inject(HttpClient);

  private readonly apiUrl =
    environment.apiUrl;

  getNotifications() {

    return this.http.get<
      NotificationResponse[]
    >(
      `${this.apiUrl}/notifications`
    );

  }

  getSummary() {

    return this.http.get<
      NotificationSummaryResponse
    >(
      `${this.apiUrl}/notifications/summary`
    );

  }

  retryNotification(
    notificationId: string
  ) {

    return this.http.post(
      `${this.apiUrl}/notifications/${notificationId}/retry`,
      {}
    );

  }

}