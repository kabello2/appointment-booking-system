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
  ReportingService
} from '../../services/reporting.service';

import {
  AppointmentStatusSummaryResponse
} from '../../models/appointment-status-summary-response.model';

@Component({
  selector: 'app-report-dashboard',
  standalone: true,
  imports: [
    MatCardModule
  ],
  templateUrl:
    './report-dashboard.component.html',
  styleUrl:
    './report-dashboard.component.scss'
})
export class ReportDashboardComponent
  implements OnInit {

  private readonly reportingService =
    inject(ReportingService);

  readonly report =
    signal<
      AppointmentStatusSummaryResponse | null
    >(null);

  ngOnInit(): void {

    this.reportingService
      .getStatusReport()
      .subscribe({

        next: report => {

          this.report.set(report);

        }

      });

  }

}