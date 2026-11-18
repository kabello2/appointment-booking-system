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
    MatInputModule
  ],
  templateUrl: './branch-list.component.html',
  styleUrl: './branch-list.component.scss'
})
export class BranchListComponent
implements OnInit {

  private readonly branchService = inject(BranchService);
  private readonly dialog = inject(MatDialog);

  readonly allBranches = signal<BranchResponse[]>([]);
  readonly branches = signal<BranchResponse[]>([]);

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

private loadBranches(): void {

  this.branchService
    .getBranches()
    .subscribe({

      next: branches => {

        this.allBranches.set(branches);

        this.branches.set(branches);

      }

    });

}

filterBranches(
  event: Event
): void {

  const value =
    (event.target as HTMLInputElement)
      .value
      .toLowerCase();

  const filtered =
    this.allBranches()
      .filter(branch =>

        branch.branchCode
          .toLowerCase()
          .includes(value)

        ||

        branch.name
          .toLowerCase()
          .includes(value)

        ||

        branch.city
          .toLowerCase()
          .includes(value)

      );

  this.branches.set(filtered);

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