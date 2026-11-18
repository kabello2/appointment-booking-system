import { Routes } from '@angular/router';

import { DashboardComponent } from './features/dashboard/dashboard.component';
import { MainLayoutComponent } from './layouts/main-layout/main-layout.component';
import { AuthLayoutComponent } from './layouts/auth-layout/auth-layout.component';
import { LoginComponent } from './features/auth/login/login.component';
import { authGuard } from './core/guards/auth.guard';
import { UserListComponent } from './features/users/pages/user-list/user-list.component';
import { BranchListComponent } from './features/branches/pages/branch-list.component';
import { ServiceListComponent } from './features/services/pages/service-list.component';
import { AppointmentListComponent } from './features/appointments/pages/appointment-list.component';
import { ReportDashbordListComponent } from './features/reports/pages/report-dashboard.component';
import { NotificationListComponent } from './features/notifications/pages/notification-list.component';
import { UserCreateComponent } from './features/users/pages/user-create/user-create.component';
import {
  UserEditComponent
}
from './features/users/pages/user-edit/user-edit.component';

import { BranchCreateComponent }
from './features/branches/pages/branch-create/branch-create.component';

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
    component: BranchListComponent
  },
  
  {
    path: 'services',
    component: ServiceListComponent
  },
  
  {
    path: 'appointments',
    component: AppointmentListComponent
  },
  
  {
    path: 'reports',
    component: ReportDashbordListComponent
  },
  
  {
    path: 'notifications',
    component: NotificationListComponent
  },
{
  path: 'branches',
  component: BranchListComponent,
  canActivate: [authGuard]
},
{
  path: 'branches/create',
  component: BranchCreateComponent,
  canActivate: [authGuard]
}
];