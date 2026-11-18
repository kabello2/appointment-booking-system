import { NavigationItem } from "../../core/models/navigation-item.model";

export const NAVIGATION_ITEMS:
NavigationItem[] = [

  {
    label: 'Dashboard',
    route: '/dashboard',
    icon: 'dashboard',
    roles: [
      'ROLE_ADMIN',
      'CONSULTANT',
      'CUSTOMER'
    ]
  },

  {
  label: 'Appointments',
  route: '/appointments',
  icon: 'event',
  roles: [
    'ROLE_ADMIN',
    'CONSULTANT',
    'CUSTOMER'
  ]
},

  {
    label: 'Appointment Dashboard',
    route: '/appointments/dashboard',
    icon: 'event',
    roles: [
      'ROLE_ADMIN',
      'CONSULTANT',
      'CUSTOMER'
    ]
  },

  {
  label: 'My Schedule',
  route: '/appointments/schedule',
  icon: 'calendar_month',
  roles: [
    'ROLE_CONSULTANT'
  ]
},

  {
  label: 'My Work Queue',
  route: '/appointments/work-queue',
  icon: 'assignment',
  roles: [
    'CONSULTANT'
  ]
},

  {
    label: 'Branches',
    route: '/branches',
    icon: 'business',
    roles: [
      'ROLE_ADMIN'
    ]
  },

  {
    label: 'Services',
    route: '/services',
    icon: 'build',
    roles: [
      'ROLE_ADMIN'
    ]
  },

  {
    label: 'Users',
    route: '/users',
    icon: 'group',
    roles: [
      'ROLE_ADMIN'
    ]
  },

  {
    label: 'Reports',
    route: '/reports',
    icon: 'bar_chart',
    roles: [
      'ROLE_ADMIN'
    ]
  },

  {
  label: 'Branch Report',
  route: '/reports/branches',
  icon: 'analytics',
  roles: [
    'ROLE_ADMIN'
  ]
},
{
  label: 'Service Report',
  route: '/reports/services',
  icon: 'insights',
  roles: [
    'ROLE_ADMIN'
  ]
},

{
  label: 'Daily Report',
  route: '/reports/daily',
  icon: 'calendar_view_day',
  roles: [
    'ROLE_ADMIN'
  ]
}

];