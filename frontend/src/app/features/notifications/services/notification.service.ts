import { Injectable, inject } from '@angular/core';

import { HttpClient } from '@angular/common/http';

import { environment } from '../../../../environments/environment';

import {
  NotificationResponse
} from '../models/notification-response.model';

import {
  NotificationSummaryResponse
} from '../models/notification-summary-response.model';

import { PageResponse } from '../../../core/models/page-response.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  private readonly http =
    inject(HttpClient);

  private readonly apiUrl =
    environment.apiUrl;

  getNotifications(
    page: number = 0,
    size: number = 10
  ): Observable<PageResponse<NotificationResponse>> {

    return this.http.get<PageResponse<NotificationResponse>>(
      `${this.apiUrl}/notifications?page=${page}&size=${size}`
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