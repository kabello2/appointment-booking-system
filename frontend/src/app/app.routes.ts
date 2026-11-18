import { Routes } from '@angular/router';

import { DashboardComponent } from './features/dashboard/dashboard.component';
import { MainLayoutComponent } from './layouts/main-layout/main-layout.component';
import { AuthLayoutComponent } from './layouts/auth-layout/auth-layout.component';
import { LoginComponent } from './features/auth/login/login.component';
import { authGuard } from './core/guards/auth.guard';
import { UserListComponent } from './features/users/pages/user-list/user-list.component';
import { BranchListComponent } from './features/branches/pages/branch-list/branch-list.component';
import { ServiceListComponent } from './features/services/pages/service-list/service-list.component';
import { ReportDashboardComponent } from './features/reports/pages/report-dashboard/report-dashboard.component';
import { UserCreateComponent } from './features/users/pages/user-create/user-create.component';
import {
  UserEditComponent
}
from './features/users/pages/user-edit/user-edit.component';

import { BranchCreateComponent }
from './features/branches/pages/branch-create/branch-create.component';
import {
  BranchEditComponent
} from './features/branches/pages/branch-edit/branch-edit.component';

import {
  ServiceCreateComponent
} from './features/services/pages/service-create/service-create.component';
import {
  AppointmentListComponent
} from './features/appointments/pages/appointment-list/appointment-list.component';
import {
  AppointmentCreateComponent
} from './features/appointments/pages/appointment-create/appointment-create.component';

import {
  AppointmentDashboardComponent
}
from './features/appointments/pages/appointment-dashboard/appointment-dashboard.component';

import {
  AppointmentDetailsComponent
} from './features/appointments/pages/appointment-details/appointment-details.component';

import {
  AppointmentRescheduleComponent
} from './features/appointments/pages/appointment-reschedule/appointment-reschedule.component';

import {
  ConsultantWorkQueueComponent
} from './features/appointments/pages/consultant-work-queue/consultant-work-queue.component';

import {
  AppointmentScheduleComponent
} from './features/appointments/pages/appointment-schedule/appointment-schedule.component';

import {
  NotificationDashboardComponent
} from './features/notifications/pages/notification-dashboard/notification-dashboard.component';

import {
  NotificationListComponent
} from './features/notifications/pages/notification-list/notification-list.component';

import {
  BranchReportComponent
} from './features/reports/pages/branch-report/branch-report.component';

import {
  ServiceReportComponent
} from './features/reports/pages/service-report/service-report.component';

import {
  AppointmentReportComponent
} from './features/reports/pages/appointment-report/appointment-report.component';

export const routes: Routes = [

  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full'
  },

  {
    path: 'auth',
    component: AuthLayoutComponent,

    children: [
      {
        path: 'login',
        component: LoginComponent
      }
    ]
  },

  {
    path: '',
    component: MainLayoutComponent,

    children: [
      {
        path: 'dashboard',
        component: DashboardComponent,
        canActivate: [authGuard]
      }
    ]
  },
  
  {
    path: 'users',
    component: UserListComponent,
    canActivate: [authGuard],
  },

  {
    path: 'users/create',
    component: UserCreateComponent,
    canActivate: [authGuard]
  },
  
  {
    path: 'users/:id/edit',
    component: UserEditComponent,
    canActivate: [authGuard]
  },
  
  {
    path: 'branches',
    component: BranchListComponent,
    canActivate: [authGuard]
  },
  {
  path: 'branches/:id/edit',
  component: BranchEditComponent,
  canActivate: [authGuard]
},

{
  path: 'branches/create',
  component: BranchCreateComponent,
  canActivate: [authGuard]
},
  
  {
    path: 'services',
    component: ServiceListComponent,
    canActivate: [authGuard]
  },
  
  {
    path: 'services/create',
    component: ServiceCreateComponent,
  canActivate: [authGuard]
  },
  
  {
    path: 'appointments',
    component: AppointmentListComponent,
  canActivate: [authGuard]
  },

  {
  path: 'appointments/create',
  component: AppointmentCreateComponent,
  canActivate: [authGuard]
},

{
  path: 'appointments/dashboard',
  component: AppointmentDashboardComponent,
  canActivate: [authGuard]
},

{
  path: 'appointments/:id',
  component: AppointmentDetailsComponent,
  canActivate: [authGuard]
},

{
  path: 'appointments/:id/reschedule',
  component: AppointmentRescheduleComponent,
  canActivate: [authGuard]
},

{
  path: 'appointments/schedule',
  component: AppointmentScheduleComponent,
  canActivate: [authGuard]
},

{
  path: 'appointments/work-queue',
  component: ConsultantWorkQueueComponent,
  canActivate: [authGuard]
},
  
  {
    path: 'reports',
    component: ReportDashboardComponent,
  canActivate: [authGuard]
  },

  {
  path: 'reports/branches',
  component: BranchReportComponent,
  canActivate: [authGuard]
},

{
  path: 'reports/services',
  component: ServiceReportComponent,
  canActivate: [authGuard]
},

{
  path: 'reports/daily',
  component: AppointmentReportComponent,
  canActivate: [authGuard]
},
  
  {
    path: 'notifications',
    component: NotificationListComponent,
  canActivate: [authGuard]
  },

{
  path: 'notifications/dashboard',
  component: NotificationDashboardComponent,
  canActivate: [authGuard]
}
];