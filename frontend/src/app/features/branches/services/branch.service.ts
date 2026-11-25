import { Injectable, inject } from '@angular/core';

import { HttpClient } from '@angular/common/http';

import { environment } from '../../../../environments/environment';

import { BranchResponse } from '../models/branch-response.model';

import { BranchRequest } from '../models/branch-request.model';
import { PageResponse } from '../../../core/models/page-response.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BranchService {

  private readonly http =
    inject(HttpClient);

  private readonly apiUrl =
    environment.apiUrl;

  getBranches(
    page: number = 0,
    size: number = 10
  ): Observable<PageResponse<BranchResponse>> {

    return this.http.get<PageResponse<BranchResponse>>(
      `${environment.apiUrl}/branches?page=${page}&size=${size}`
    );
  }

  getBranchById(
    branchId: string
  ) {

    return this.http.get<BranchResponse>(

      `${this.apiUrl}/branches/${branchId}`

    );

  }

  createBranch(
    request: BranchRequest
  ) {

    return this.http.post<BranchResponse>(

      `${this.apiUrl}/branches`,
      request

    );

  }

  updateBranch(
    branchId: string,
    request: BranchRequest
  ) {

    return this.http.put(

      `${this.apiUrl}/branches/${branchId}`,
      request

    );

  }

  deleteBranch(
    branchId: string
  ) {

    return this.http.delete(

      `${this.apiUrl}/branches/${branchId}`

    );

  }

}