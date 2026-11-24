import {
    Component,
    inject
} from '@angular/core';

import {
    FormBuilder,
    ReactiveFormsModule,
    Validators
} from '@angular/forms';

import {
    Router,
    RouterLink
} from '@angular/router';

import {
    MatCardModule
} from '@angular/material/card';

import {
    MatFormFieldModule
} from '@angular/material/form-field';

import {
    MatInputModule
} from '@angular/material/input';

import {
    MatButtonModule
} from '@angular/material/button';

import { AuthService } from '../../../core/services/auth.service';

import {
    SnackbarService
} from '../../../core/services/snackbar.service';
import { ValidationPatterns } from '../../../shared/validators/validation.constants';
import { FormErrorUtils } from '../../../shared/utils/form-error.utils';

@Component({
    selector: 'app-register',
    standalone: true,
    imports: [
        ReactiveFormsModule,
        RouterLink,
        MatCardModule,
        MatFormFieldModule,
        MatInputModule,
        MatButtonModule
    ],
    templateUrl: './register.component.html',
    styleUrl: './register.component.scss'
})
export class RegisterComponent {

    private readonly fb =
        inject(FormBuilder);

    private readonly router =
        inject(Router);

    private readonly authService =
        inject(AuthService);

    private readonly snackbar =
        inject(SnackbarService);

    validationErrors:
        Record<string, string> = {};

    readonly form =
        this.fb.group({

            firstName: [
                '', [
                    Validators.required,
                    Validators.maxLength(100)
                ]
            ],

            lastName: [
                '',
                [
                    Validators.required,
                    Validators.maxLength(100)
                ]
            ],

            email: [
                '',
                [
                    Validators.required,
                    Validators.email
                ]
            ],

            phoneNumber: [
                '', [
                    Validators.required,
                    Validators.pattern(
                        ValidationPatterns.SOUTH_AFRICAN_PHONE
                    )
                ]
            ],

            password: [
                '',
                [
                    Validators.required,
                    Validators.pattern(
                        ValidationPatterns.PASSWORD_COMPLEXITY
                    )
                ]
            ],

            confirmPassword: [
                '',
                Validators.required
            ]

        });

    ngOnInit(): void {

        Object.keys(this.form.controls)
            .forEach(controlName => {

                this.form
                    .get(controlName)
                    ?.valueChanges
                    .subscribe(() => {

                        FormErrorUtils.clearServerErrors(
                            this.form.get(controlName)
                        );

                    });

            });

    }

    register(): void {

        if (this.form.invalid) {

            this.form.markAllAsTouched();

            return;

        }

        const password =
            this.form.controls.password.value;

        const confirmPassword =
            this.form.controls.confirmPassword.value;

        if (password !== confirmPassword) {

            this.snackbar.error(
                'Passwords do not match'
            );

            return;

        }

        this.authService
            .register(
                this.form.getRawValue() as any
            )
            .subscribe({

                next: () => {

                    this.snackbar.success(
                        'Registration successful'
                    );

                    this.router.navigate([
                        '/auth/login'
                    ]);

                }, error: error => {


                    FormErrorUtils.applyServerErrors(
                        this.form,
                        error.error?.validationErrors ?? {}
                    );
                }



            });

    }

}