import {
  Pipe,
  PipeTransform
} from '@angular/core';

@Pipe({
  name: 'roleDisplay',
  standalone: true
})
export class RoleDisplayPipe
implements PipeTransform {

  transform(
    value: string
  ): string {

    switch (value) {

      case 'ROLE_ADMIN':
        return 'Administrator';

      case 'ROLE_CONSULTANT':
        return 'Consultant';

      case 'ROLE_CUSTOMER':
        return 'Customer';

      default:
        return value;

    }

  }

}