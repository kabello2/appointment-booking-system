import { Injectable, inject } from '@angular/core';

import { HttpClient } from '@angular/common/http';

import { environment } from '../../../../environments/environment';

import { AppointmentResponse }
  from '../models/appointment-response.model';

import { BookAppointmentRequest }
  from '../models/book-appointment-request.model';

import { AvailableSlotResponse }
  from '../models/available-slot-response.model';

import { DashboardResponse }
  from '../models/dashboard-response.model';

import {
  RescheduleAppointmentRequest
} from '../models/reschedule-appointment-request.model';

import { PageResponse } from '../../../core/models/page-response.model';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class AppointmentService {

  private readonly http =
    inject(HttpClient);

  private readonly apiUrl =
    environment.apiUrl;

  getAppointments(
    page: number = 0,
    size: number = 10
  ): Observable<PageResponse<AppointmentResponse>> {

    return this.http.get<PageResponse<AppointmentResponse>>(

      `${this.apiUrl}/appointments?page=${page}&size=${size}`

    );

  }

  bookAppointment(
    request: BookAppointmentRequest
  ) {

    return this.http.post<AppointmentResponse>(

      `${this.apiUrl}/appointments`,
      request

    );

  }

  getAppointment(
    appointmentId: string
  ) {

    return this.http.get<AppointmentResponse>(

      `${this.apiUrl}/appointments/${appointmentId}`

    );

  }

  confirmAppointment(
    appointmentId: string
  ) {

    return this.http.put(

      `${this.apiUrl}/appointments/${appointmentId}/confirm`,
      {}

    );

  }

  completeAppointment(
    appointmentId: string
  ) {

    return this.http.put(

      `${this.apiUrl}/appointments/${appointmentId}/complete`,
      {}

    );

  }

  cancelAppointment(
    appointmentId: string
  ) {

    return this.http.put(

      `${this.apiUrl}/appointments/${appointmentId}/cancel`,
      {}

    );

  }

  getUpcomingAppointments() {

    return this.http.get<AppointmentResponse[]>(

      `${this.apiUrl}/appointments/my/upcoming`

    );

  }

  getDashboard() {

    return this.http.get<DashboardResponse>(

      `${this.apiUrl}/appointments/dashboard/me`

    );

  }

  getAvailableSlots(
    branchId: string,
    appointmentDate: string
  ) {

    return this.http.get<AvailableSlotResponse[]>(

      `${this.apiUrl}/appointments/available-slots`
      + `?branchId=${branchId}`
      + `&appointmentDate=${appointmentDate}`

    );

  }

  getMyDashboard() {

    return this.http.get<DashboardResponse>(

      `${this.apiUrl}/appointments/dashboard/me`

    );

  }

  getMyUpcomingAppointments() {

    return this.http.get<AppointmentResponse[]>(

      `${this.apiUrl}/appointments/my/upcoming`

    );

  }

  rescheduleAppointment(
    appointmentId: string,
    request: RescheduleAppointmentRequest
  ) {

    return this.http.put<AppointmentResponse>(

      `${this.apiUrl}/appointments/${appointmentId}/reschedule`,

      request

    );

  }

}