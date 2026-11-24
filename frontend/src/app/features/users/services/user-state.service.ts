import {
    Injectable,
    signal
} from '@angular/core';

import { UserResponse } from '../models/user-response.model';

@Injectable({
    providedIn: 'root'
})
export class UserStateService {

    readonly users =
        signal<UserResponse[]>([]);

    readonly selectedUser =
        signal<UserResponse | null>(
            null
        );

    setUser(
        users: UserResponse[]
    ): void {
        this.users.set(
            users
        );
    };

    addUser(
        user: UserResponse
    ): void {

        this.users.update(
            users => [
                user,
                ...users
            ]
        );
    };

    updateUser(
        updatedUser: UserResponse
    ): void {

        this.users.update(
            users =>
                users.map(
                    user =>
                        user.id === updatedUser.id
                            ? updatedUser
                            : user
                )
        );

        if (
            this.selectedUser()?.id
            === updatedUser.id
        ) {

            this.selectedUser.set(
                updatedUser
            );

        }

    }

    removeUser(
        userId: string
    ): void {

        this.users.update(
            users =>
                users.filter(
                    user =>
                        user.id !== userId
                )
        );

    }
}