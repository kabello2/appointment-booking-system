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
  ServicePopularityResponse
} from '../../models/service-popularity-response.model';

import {
  ReportingService
} from '../../services/reporting.service';

@Component({
  selector: 'app-service-report',
  standalone: true,
  imports: [
    MatTableModule
  ],
  templateUrl: './service-report.component.html',
  styleUrl: './service-report.component.scss'
})
export class ServiceReportComponent
  implements OnInit {

  private readonly reportingService =
    inject(ReportingService);

  readonly services =
    signal<ServicePopularityResponse[]>([]);

  displayedColumns = [
    'rank',
    'serviceCode',
    'serviceName',
    'bookingCount'
  ];

  ngOnInit(): void {

    this.loadReport();

  }

  private loadReport(): void {

    this.reportingService
      .getServicePopularityReport()
      .subscribe({

        next: report => {

          const sorted =
            [...report]
              .sort(
                (first, second) =>
                  second.bookingCount -
                  first.bookingCount
              );

          this.services.set(sorted);

        }

      });

  }

}