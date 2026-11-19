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
  AuthState
} from '../../core/state/auth.state';

import {
  DashboardService
} from './services/dashboard.service';

import {
  DashboardSummary
} from './models/dashboard-summary.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    MatCardModule
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent
  implements OnInit {

  protected readonly authState =
    inject(AuthState);

  private readonly dashboardService =
    inject(DashboardService);

  readonly summary =
    signal<DashboardSummary | null>(
      null
    );

  ngOnInit(): void {

    this.dashboardService
      .getSummary()
      .subscribe(summary => {

        this.summary.set(summary);

      });

  }

}