import {
    Injectable,
    signal
} from '@angular/core';

import { BranchResponse } from '../models/branch-response.model';

@Injectable({
    providedIn: 'root'
})
export class BranchStateService {

    readonly branches =
        signal<BranchResponse[]>([]);

    readonly selectedBranch =
        signal<BranchResponse | null>(
            null
        );

    setBranches(
        branches: BranchResponse[]
    ): void {
        this.branches.set(
            branches
        );
    };

    addBranch(
        branch: BranchResponse
    ): void {

        this.branches.update(
            appointments => [
                branch,
                ...appointments
            ]
        );
    };

    updateBranch(
        updatedBranch: BranchResponse
    ): void {

        this.branches.update(
            branches =>
                branches.map(
                    branch =>
                        branch.id === updatedBranch.id
                            ? updatedBranch
                            : branch
                )
        );

        if (
            this.selectedBranch()?.id
            === updatedBranch.id
        ) {

            this.selectedBranch.set(
                updatedBranch
            );

        }

    }

    removeBranch(
        branchId: string
    ): void {

        this.branches.update(
            branches =>
                branches.filter(
                    branch =>
                        branch.id !== branchId
                )
        );

    }
}