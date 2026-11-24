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
import {
  LoadingSpinnerComponent
} from '../../../../shared/components/loading-spinner/loading-spinner.component';
import {
  RoleDisplayPipe
} from '../../../../shared/pipes/role-display.pipe';

import { MatDialog } from '@angular/material/dialog';
import { UserStateService } from '../../services/user-state.service';

import { MatTableDataSource } from '@angular/material/table';

import { ConfirmDialogComponent }
  from '../../../../shared/components/confirm-dialog/confirm-dialog.component';

import {
  ViewChild,
  AfterViewInit
} from '@angular/core';

import {
  MatPaginator,
  MatPaginatorModule
} from '@angular/material/paginator';

import {
  MatSort,
  MatSortModule
} from '@angular/material/sort';

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [
    RouterLink,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    LoadingSpinnerComponent,
    RoleDisplayPipe,
    MatPaginatorModule,
    MatSortModule
  ],
  templateUrl: './user-list.component.html',
  styleUrl: './user-list.component.scss'
})
export class UserListComponent implements OnInit, AfterViewInit {

  @ViewChild(MatPaginator)
  paginator!: MatPaginator;

  @ViewChild(MatSort)
  sort!: MatSort;
  readonly loading = signal(false);
  private readonly dialog = inject(MatDialog);
  private readonly userService =
    inject(UserService);

  private readonly userState =
    inject(UserStateService);

  readonly dataSource =
    new MatTableDataSource<UserResponse>();


  totalElements = signal(0);
  totalPages = signal(0);
  pageIndex = signal(0);
  pageSize = signal(10);

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

  ngAfterViewInit(): void {

    this.dataSource.paginator =
      this.paginator;

    this.paginator.page.subscribe(
      () => this.onPageChange()
    );

  }


  onPageChange(): void {

    this.pageIndex.set(
      this.paginator.pageIndex
    );

    this.pageSize.set(
      this.paginator.pageSize
    );

    this.loadUsers();

  }

  private loadUsers(): void {
    this.loading.set(true);
    this.userService
      .getUsers(
        this.pageIndex(),
        this.pageSize())
      .subscribe({

        next: response => {

          this.dataSource.data = response.items;

          this.totalPages.set(
            response.totalPages
          );

          this.totalElements.set(
            response.totalElements
          );
          this.loading.set(false);
        },

        error: () => {

          this.loading.set(false);

        }

      });

  }

  filterUsers(
    event: Event
  ): void {

    const filterValue =
      (event.target as HTMLInputElement)
        .value;

    this.dataSource.filter =
      filterValue
        .trim()
        .toLowerCase();

  }

  toggleStatus(
    user: UserResponse
  ): void {

    const dialogRef =
      this.dialog.open(
        ConfirmDialogComponent,
        {
          width: '400px',

          data: {

            title:
              `${user.enabled ? 'Disable' : 'Enable'} user`,

            message:
              `${user.enabled ? 'Disable' : 'Enable'} ${user.firstName} ${user.lastName}?`

          }

        }
      );

    dialogRef
      .afterClosed()
      .subscribe(result => {

        if (!result) {
          return;
        }

        const request =
          user.enabled
            ? this.userService.disableUser(user.id)
            : this.userService.enableUser(user.id);

        request.subscribe({

          next: () => {
            this.userState.updateUser(user);
          }

        });

      });

  }

}