import { NavigationItem } from "../../core/models/navigation-item.model";

export const NAVIGATION_ITEMS:
NavigationItem[] = [

  {
    label: 'Dashboard',
    route: '/dashboard',
    icon: 'dashboard',
    roles: [
      'ADMIN',
      'CONSULTANT',
      'CUSTOMER'
    ]
  },

  {
    label: 'Appointments',
    route: '/appointments',
    icon: 'event',
    roles: [
      'ADMIN',
      'CONSULTANT'
    ]
  },

  {
    label: 'Branches',
    route: '/branches',
    icon: 'business',
    roles: [
      'ADMIN'
    ]
  },

  {
    label: 'Services',
    route: '/services',
    icon: 'build',
    roles: [
      'ADMIN'
    ]
  },

  {
    label: 'Users',
    route: '/users',
    icon: 'group',
    roles: [
      'ADMIN'
    ]
  },

  {
    label: 'Reports',
    route: '/reports',
    icon: 'bar_chart',
    roles: [
      'ADMIN'
    ]
  }
];