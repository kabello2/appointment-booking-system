import {
  Component,
  computed,
  inject
} from '@angular/core';

import {
  RouterLink
} from '@angular/router';

import {
  MatListModule
} from '@angular/material/list';

import {
  MatIconModule
} from '@angular/material/icon';

import {
  AuthState
} from '../../../core/state/auth.state';

import {
  NAVIGATION_ITEMS
} from '../../navigation/navigation.config';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [
    RouterLink,
    MatListModule,
    MatIconModule
  ],
  templateUrl: './sidebar.component.html'
})
export class SidebarComponent {

  private readonly authState =
    inject(AuthState);

  readonly menuItems =
    computed(() => {

      const user =
        this.authState.currentUser();

      if (!user) {
        return [];
      }
      console.log(
        this.authState.currentUser()
      );


      return NAVIGATION_ITEMS.filter(
        item =>
          item.roles.includes(
            user.role
          )
      );

    });

}