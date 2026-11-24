import { Injectable, inject } from '@angular/core';

import { HttpClient } from '@angular/common/http';

import { environment } from '../../../../environments/environment';

import {
  AppointmentStatusSummaryResponse
} from '../models/appointment-status-summary-response.model';

import {
  BranchAppointmentReportResponse
} from '../models/branch-appointment-report-response.model';

import {
  ServicePopularityResponse
} from '../models/service-popularity-response.model';

import {
  DailyAppointmentReportResponse
} from '../models/daily-appointment-report-response.model';

@Injectable({
  providedIn: 'root'
})
export class ReportingService {

  private readonly http =
    inject(HttpClient);

  private readonly apiUrl =
    environment.apiUrl;

  getStatusReport() {

    return this.http.get<
      AppointmentStatusSummaryResponse
    >(
      `${this.apiUrl}/reports/appointments/status`
    );

  }

  getBranchReport() {

    return this.http.get<
      BranchAppointmentReportResponse[]
    >(
      `${this.apiUrl}/reports/appointments/branches`
    );

  }

  getServicePopularityReport() {

    return this.http.get<
      ServicePopularityResponse[]
    >(
      `${this.apiUrl}/reports/services/popularity`
    );

  }

  getDailyAppointmentReport() {

    return this.http.get<
      DailyAppointmentReportResponse[]
    >(
      `${this.apiUrl}/reports/appointments/daily`
    );

  }

}