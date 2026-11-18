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
  DailyAppointmentReportResponse
} from '../../models/daily-appointment-report-response.model';

import {
  ReportingService
} from '../../services/reporting.service';

@Component({
  selector: 'app-appointment-report',
  standalone: true,
  imports: [
    MatTableModule
  ],
  templateUrl: './appointment-report.component.html',
  styleUrl: './appointment-report.component.scss'
})
export class AppointmentReportComponent
implements OnInit {

  private readonly reportingService =
    inject(ReportingService);

  readonly dailyAppointments =
    signal<DailyAppointmentReportResponse[]>([]);

  displayedColumns = [
    'appointmentDate',
    'appointmentCount'
  ];

  ngOnInit(): void {

    this.loadReport();

  }

  private loadReport(): void {

    this.reportingService
      .getDailyAppointmentReport()
      .subscribe({

        next: report => {

          const sorted =
            [...report]
              .sort((first, second) =>
                first.appointmentDate.localeCompare(
                  second.appointmentDate
                )
              );

          this.dailyAppointments.set(
            sorted
          );

        }

      });

  }

}