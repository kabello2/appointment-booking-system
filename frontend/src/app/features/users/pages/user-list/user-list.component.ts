import {
  Component,
  OnInit,
  inject,
  signal
} from '@angular/core';

import { RouterLink } from '@angular/router';

import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

import { UserService } from '../../services/user.service';
import { UserResponse } from '../../models/user-response.model';

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [
    RouterLink,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule
  ],
  templateUrl: './user-list.component.html',
  styleUrl: './user-list.component.scss'
})
export class UserListComponent implements OnInit {

  private readonly userService =
    inject(UserService);

  readonly allUsers =
    signal<UserResponse[]>([]);

  readonly users =
    signal<UserResponse[]>([]);

  displayedColumns = [
    'firstName',
    'lastName',
    'email',
    'role',
    'enabled',
    'actions'
  ];

  ngOnInit(): void {

    this.loadUsers();

  }

  private loadUsers(): void {

    this.userService
      .getUsers()
      .subscribe(users => {

        this.allUsers.set(users);

        this.users.set(users);

      });

  }

  filterUsers(
    event: Event
  ): void {

    const value =
      (event.target as HTMLInputElement)
        .value
        .toLowerCase();

    const filtered =
      this.allUsers().filter(user =>

        user.firstName
          .toLowerCase()
          .includes(value)

        ||

        user.lastName
          .toLowerCase()
          .includes(value)

        ||

        user.email
          .toLowerCase()
          .includes(value)

      );

    this.users.set(filtered);

  }

  toggleStatus(
    user: UserResponse
  ): void {

    const confirmed =
      confirm(
        `${user.enabled ? 'Disable' : 'Enable'} ${user.firstName}?`
      );

    if (!confirmed) {
      return;
    }

    const request =
      user.enabled
        ? this.userService.disableUser(user.id)
        : this.userService.enableUser(user.id);

    request.subscribe({

      next: () => {

        this.loadUsers();

      }

    });

  }

}