import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Observable } from 'rxjs';

import { environment } from '../../../../environments/environment';

import { DashboardSummary }
from '../models/dashboard-summary.model';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  private readonly http =
    inject(HttpClient);

  private readonly apiUrl =
    environment.apiUrl;

  getSummary():
    Observable<DashboardSummary> {

    return this.http.get<DashboardSummary>(
      `${this.apiUrl}/reports/dashboard`
    );

  }

}