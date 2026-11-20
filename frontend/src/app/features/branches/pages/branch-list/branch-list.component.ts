import {
  Component,
  OnInit,
  inject,
  signal
} from '@angular/core';

import {
  RouterLink
} from '@angular/router';

import {
  MatTableModule
} from '@angular/material/table';

import {
  MatButtonModule
} from '@angular/material/button';

import {
  MatIconModule
} from '@angular/material/icon';

import {
  MatDialog,
  MatDialogModule
} from '@angular/material/dialog';

import {
  MatFormFieldModule
} from '@angular/material/form-field';

import {
  MatInputModule
} from '@angular/material/input';

import {
  ConfirmDialogComponent
} from '../../../../shared/components/confirm-dialog/confirm-dialog.component';


import {
  BranchService
} from '../../services/branch.service';

import {
  BranchResponse
} from '../../models/branch-response.model';

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

import {
  MatTableDataSource
} from '@angular/material/table';

@Component({
  selector: 'app-branch-list',
  standalone: true,
  imports: [
    
  RouterLink,
  MatTableModule,
  MatButtonModule,
  MatIconModule,
  MatDialogModule,
  MatFormFieldModule,
  MatInputModule,
  MatPaginatorModule,
  MatSortModule
  ],
  templateUrl: './branch-list.component.html',
  styleUrl: './branch-list.component.scss'
})
export class BranchListComponent
  implements OnInit, AfterViewInit {

  @ViewChild(MatPaginator)
paginator!: MatPaginator;

@ViewChild(MatSort)
sort!: MatSort;

  private readonly branchService = inject(BranchService);
  private readonly dialog = inject(MatDialog);

  readonly dataSource =
  new MatTableDataSource<BranchResponse>();

  displayedColumns = [
    'branchCode',
    'name',
    'city',
    'contactNumber',
    'active',
    'actions'
  ];

  ngOnInit(): void {

    this.loadBranches();

  }

  ngAfterViewInit(): void {

  this.dataSource.paginator =
    this.paginator;

  this.dataSource.sort =
    this.sort;

}

  private loadBranches(): void {

    this.branchService
      .getBranches()
      .subscribe({

        next: branches => {
this.dataSource.data =
  branches;

        }

      });

  }

  filterBranches(
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

  deleteBranch(
    branch: BranchResponse
  ): void {

    const dialogRef =
      this.dialog.open(
        ConfirmDialogComponent,
        {
          width: '400px',

          data: {

            title: 'Delete Branch',

            message:
              `Are you sure you want to delete ${branch.name}?`

          }
        }
      );

    dialogRef
      .afterClosed()
      .subscribe(result => {

        if (!result) {
          return;
        }

        this.branchService
          .deleteBranch(branch.id)
          .subscribe({

            next: () => {

              this.loadBranches();

            }

          });

      });

  }

}