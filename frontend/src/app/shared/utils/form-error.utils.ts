import {
    AbstractControl,
    FormGroup
} from '@angular/forms';

export class FormErrorUtils {

    static applyServerErrors(
        form: FormGroup,
        validationErrors: Record<string, string>
    ): void {

        Object.entries(validationErrors)
            .forEach(([field, message]) => {

                const control =
                    form.get(field);

                if (control) {

                    control.setErrors({
                        ...(control.errors ?? {}),
                        server: message
                    });

                    control.markAsTouched();

                }

            });

    }

    static clearServerErrors(
        control: AbstractControl | null
    ): void {

        if (!control?.errors?.['server']) {
            return;
        }

        const {
            server,
            ...remainingErrors
        } = control.errors;

        control.setErrors(
            Object.keys(remainingErrors).length > 0
                ? remainingErrors
                : null
        );

    }

}