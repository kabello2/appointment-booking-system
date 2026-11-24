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
  BranchAppointmentReportResponse
} from '../../models/branch-appointment-report-response.model';

import {
  ReportingService
} from '../../services/reporting.service';

@Component({
  selector: 'app-branch-report',
  standalone: true,
  imports: [
    MatTableModule
  ],
  templateUrl: './branch-report.component.html',
  styleUrl: './branch-report.component.scss'
})
export class BranchReportComponent
  implements OnInit {

  private readonly reportingService =
    inject(ReportingService);

  readonly branches =
    signal<BranchAppointmentReportResponse[]>([]);

  displayedColumns = [
    'branchCode',
    'branchName',
    'appointmentCount'
  ];

  ngOnInit(): void {

    this.loadReport();

  }

  private loadReport(): void {

    this.reportingService
      .getBranchReport()
      .subscribe({

        next: report => {

          const sorted =
            [...report]
              .sort(
                (first, second) =>
                  second.appointmentCount -
                  first.appointmentCount
              );

          this.branches.set(sorted);

        }

      });

  }

}