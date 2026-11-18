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
    MatIconModule
  ],
  templateUrl: './branch-list.component.html',
  styleUrl: './branch-list.component.scss'
})
export class BranchListComponent
implements OnInit {

  private readonly branchService =
    inject(BranchService);

  readonly branches =
    signal<BranchResponse[]>([]);

  displayedColumns = [
    'branchCode',
    'name',
    'city',
    'contactNumber',
    'active',
    'actions'
  ];

  ngOnInit(): void {

    this.branchService
      .getBranches()
      .subscribe(branches => {

        this.branches.set(branches);

      });

  }

}